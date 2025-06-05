import os
import random
import yaml
import shutil
import re



def distribute_dataset_with_labels(dataset_dir, train_target_count=1300, val_target_count=200, test_target_count=100):
    """
    Distributes a dataset into train, val, and test folders, ensuring the target counts,
    and also moves corresponding label files.

    Args:
        dataset_dir (str): Path to the parent dataset directory.
        train_target_count (int, optional): Target number of images in the train folder.
        val_target_count (int, optional): Target number of images in the val folder.
        test_target_count (int, optional): Target number of images in the test folder.
    """

    train_img_dir = os.path.join(dataset_dir, "train", "images")
    train_label_dir = os.path.join(dataset_dir, "train", "labels")
    val_img_dir = os.path.join(dataset_dir, "valid", "images")
    val_label_dir = os.path.join(dataset_dir, "valid", "labels")
    test_img_dir = os.path.join(dataset_dir, "test", "images")
    test_label_dir = os.path.join(dataset_dir, "test", "labels")

    def move_random_files_with_labels(source_img_dir, source_label_dir, dest_img_dir, dest_label_dir, target_count):
        """
        Moves a specified number of random image files and their corresponding label files
        from source directories to destination directories.

        Args:
            source_img_dir (str): Path to the source directory for images.
            source_label_dir (str): Path to the source directory for labels.
            dest_img_dir (str): Path to the destination directory for images.
            dest_label_dir (str): Path to the destination directory for labels.
            target_count (int): Target number of image files to remain in source_img_dir.
        """

        image_files = [f for f in os.listdir(source_img_dir) if os.path.isfile(os.path.join(source_img_dir, f))]
        source_count = len(image_files)

        if source_count > target_count:
            num_to_move = source_count - target_count
            images_to_move = random.sample(image_files, num_to_move)  # Select random image files

            if not os.path.exists(dest_img_dir):
                os.makedirs(dest_img_dir)
            if not os.path.exists(dest_label_dir):
                os.makedirs(dest_label_dir)

            for image_filename in images_to_move:
                image_source_path = os.path.join(source_img_dir, image_filename)
                image_dest_path = os.path.join(dest_img_dir, image_filename)
                label_filename = os.path.splitext(image_filename)[0] + ".txt"  # Assuming .txt labels
                label_source_path = os.path.join(source_label_dir, label_filename)
                label_dest_path = os.path.join(dest_label_dir, label_filename)
                try:
                    shutil.move(image_source_path, image_dest_path)
                    if os.path.exists(label_source_path):
                        shutil.move(label_source_path, label_dest_path)
                        # print(f"Moved: {image_source_path} and {label_source_path} to {dest_img_dir}")
                    else:
                        print(f"Warning: Label file not found: {label_source_path}")
                except FileExistsError:
                    print(f"File already exists: {image_dest_path} or {label_dest_path}. Skipping.")
                except Exception as e:
                    print(f"Error moving {image_source_path}: {e}")
        else:
            print(f"Not enough files to move from {source_img_dir} to {dest_img_dir}")

    # Distribute files
    move_random_files_with_labels(train_img_dir, train_label_dir, val_img_dir, val_label_dir, train_target_count)
    move_random_files_with_labels(val_img_dir, val_label_dir, train_img_dir, train_label_dir, val_target_count)
    move_random_files_with_labels(train_img_dir, train_label_dir, test_img_dir, test_label_dir, train_target_count)
    move_random_files_with_labels(test_img_dir, test_label_dir, train_img_dir, train_label_dir, test_target_count)

    print("Dataset distribution completed.")

def remove_excess_files_with_labels(image_dir, label_dir, threshold, image_extensions=(".jpg", ".jpeg", ".png"), label_extension=".txt"):
    """
    Removes files from the image directory until the number of files is under the given threshold,
    and also removes the corresponding label files.

    Args:
        image_dir (str): Path to the directory containing image files.
        label_dir (str): Path to the directory containing label files.
        threshold (int): The maximum number of image files allowed in image_dir.
        image_extensions (tuple, optional): Tuple of allowed image file extensions.
                                         Defaults to (".jpg", ".jpeg", ".png").
        label_extension (str, optional): The expected label file extension.
                                      Defaults to ".txt".

    Returns:
        int: The number of files removed.
    """

    image_files = [f for f in os.listdir(image_dir) if os.path.isfile(os.path.join(image_dir, f)) and f.lower().endswith(image_extensions)]
    num_images = len(image_files)
    files_removed = 0

    if num_images > threshold:
        num_to_remove = num_images - threshold
        files_to_remove = random.sample(image_files, num_to_remove)  # Select random image files

        for image_filename in files_to_remove:
            image_path = os.path.join(image_dir, image_filename)
            label_filename = os.path.splitext(image_filename)[0] + label_extension
            label_path = os.path.join(label_dir, label_filename)

            try:
                os.remove(image_path)
                files_removed += 1
                if os.path.exists(label_path):
                    os.remove(label_path)
                    # print(f"Removed image: {image_path} and label: {label_path}")
                else:
                    print(f"Warning: Corresponding label not found for {image_path}")
            except OSError as e:
                print(f"Error removing {image_path}: {e}")

    return files_removed

def check_image_label_filenames(image_dir, label_dir, image_extensions=(".jpg", ".jpeg", ".png"), label_extension=".txt"):
    """
    Checks if the image files in image_dir have corresponding label files in label_dir,
    based on filename (excluding extension).

    Args:
        image_dir (str): Path to the directory containing image files.
        label_dir (str): Path to the directory containing label files.
        image_extensions (tuple, optional): Tuple of allowed image file extensions.
                                         Defaults to (".jpg", ".jpeg", ".png").
        label_extension (str, optional): The expected label file extension.
                                      Defaults to ".txt".

    Returns:
        tuple: A tuple containing two lists:
               - missing_labels (list): List of image filenames without matching labels.
               - missing_images (list): List of label filenames without matching images.
    """

    image_files = {os.path.splitext(f)[0]: f for f in os.listdir(image_dir)
                   if os.path.isfile(os.path.join(image_dir, f)) and f.lower().endswith(image_extensions)}
    label_files = {os.path.splitext(f)[0]: f for f in os.listdir(label_dir)
                   if os.path.isfile(os.path.join(label_dir, f)) and f.lower().endswith(label_extension)}

    missing_labels = []

    for image_name in image_files:
        if image_name not in label_files:
            missing_labels.append(image_files[image_name])

    print(len(missing_labels) == 0)

def replace_whole_numbers_in_files(directory, old_number, new_number):
    """
    Replaces whole integer occurrences of old_number with new_number in all .txt files
    within a directory, avoiding replacements in floating-point numbers.

    Args:
        directory (str): The path to the directory containing the .txt files.
        old_number (str): The number to be replaced (as a string).
        new_number (str): The number to replace with (as a string).
    """

    for filename in os.listdir(directory):
        if filename.endswith(".txt"):
            filepath = os.path.join(directory, filename)
            try:
                with open(filepath, "r") as f:
                    file_content = f.read()

                # Construct the regular expression dynamically
                pattern = r"(^|\s)" + re.escape(old_number) + r"\b(?!\.)"
                # pattern = r"(?<!\.)\b" + re.escape(old_number) + r"\b(?!\.)"
                # pattern = r"^(?<!\.)" + re.escape(old_number) + r"(?!\.)(?=\s|$)"
                modified_content = re.sub(pattern, new_number, file_content)

                with open(filepath, "w") as f:
                    f.write(modified_content)

                #print(f"Replaced '{old_number}' with '{new_number}' in: {filepath}")

            except Exception as e:
                print(f"Error processing file {filepath}: {e}")

    print("Replacement process completed.")

def generate_data(dataset_path, index):
  # Distribute image
  distribute_dataset_with_labels(dataset_path)

  # Remove exceceed files:
  image_directory = dataset_path + "/train/images"
  label_directory = dataset_path + "/train/labels"
  file_threshold = 1300
  removed_count = remove_excess_files_with_labels(image_directory, label_directory, file_threshold)
  print(f"\nRemoved {removed_count} files to reach the threshold.")

  # Check if the images and labels directory have the same name files
  check_image_label_filenames(dataset_path + "/train/images", dataset_path + "/train/labels")
  check_image_label_filenames(dataset_path + "/test/images", dataset_path + "/test/labels")
  check_image_label_filenames(dataset_path + "/valid/images", dataset_path + "/valid/labels")

  if (index != 0):
    old_value = "0"
    new_value = index
    replace_whole_numbers_in_files(dataset_path + "/train/labels", old_value, new_value)
    replace_whole_numbers_in_files(dataset_path + "/test/labels", old_value, new_value)
    replace_whole_numbers_in_files(dataset_path + "/valid/labels", old_value, new_value)

def create_and_move_directories(target_dir, data_dir="/content/data"):
    """
    Creates a directory structure for train/val/test data and moves files.

    Args:
        target_dir (str): The path to the directory containing the original
                          train/valid/test subdirectories.
        data_dir (str, optional): The base directory where the new structure
                                 will be created. Defaults to "/content/data".
    """

    try:
        # Create the main data directory
        os.makedirs(data_dir, exist_ok=True)
        print(f"Created directory: {data_dir}")

        # Create train/val/test directories
        for subdir in ["train", "valid", "test"]:
            os.makedirs(os.path.join(data_dir, subdir, "labels"), exist_ok=True)
            os.makedirs(os.path.join(data_dir, subdir, "images"), exist_ok=True)
            print(f"Created directories: {os.path.join(data_dir, subdir, 'labels')} and {os.path.join(data_dir, subdir, 'images')}")

        # Move files
        for subdir in ["train", "valid", "test"]:
            source_labels = os.path.join(target_dir, subdir, "labels")
            dest_labels = os.path.join(data_dir, subdir, "labels")
            source_images = os.path.join(target_dir, subdir, "images")
            dest_images = os.path.join(data_dir, subdir, "images")

            if os.path.exists(source_labels):
                for file in os.listdir(source_labels):
                    shutil.move(os.path.join(source_labels, file), dest_labels)
                print(f"Moved files from {source_labels} to {dest_labels}")
            else:
                print(f"Source directory not found: {source_labels}")

            if os.path.exists(source_images):
                for file in os.listdir(source_images):
                    shutil.move(os.path.join(source_images, file), dest_images)
                print(f"Moved files from {source_images} to {dest_images}")
            else:
                print(f"Source directory not found: {source_images}")

        print("Directory creation and file movement completed.")

    except Exception as e:
        print(f"An error occurred: {e}")

def generate_yolov8_yaml(
    output_path,
    train_images_path,
    val_images_path,
    test_images_path,
    class_names,
    roboflow_workspace,
    roboflow_project,
    roboflow_version,
    roboflow_license="CC BY 4.0",
    roboflow_url=None,
):
    """
    Generates a YAML file in the format expected by YOLOv5 for dataset configuration.

    Args:
        output_path (str): Path to save the generated YAML file.
        train_images_path (str): Path to the training images directory.
        val_images_path (str): Path to the validation images directory.
        test_images_path (str): Path to the test images directory.
        class_names (list): A list of class names (strings).
        roboflow_workspace (str): The Roboflow workspace name.
        roboflow_project (str): The Roboflow project name.
        roboflow_version (int): The Roboflow dataset version.
        roboflow_license (str, optional): The Roboflow license. Defaults to "CC BY 4.0".
        roboflow_url (str, optional): The Roboflow dataset URL. Defaults to None.
    """

    # Manually construct the 'names' string
    names_string = f"names: {class_names}"

    data = {
        "train": train_images_path,
        "val": val_images_path,
        "test": test_images_path,
        "nc": len(class_names),
        "roboflow": {
            "workspace": roboflow_workspace,
            "project": roboflow_project,
            "version": roboflow_version,
            "license": roboflow_license,
        }
    }

    if roboflow_url:
        data["roboflow"]["url"] = roboflow_url

    try:
        # Generate the rest of the YAML using PyYAML
        yaml_content = yaml.dump(data, sort_keys=False)

        # Insert the manually constructed 'names' string
        with open(output_path, "w") as f:
            f.write(yaml_content)
            f.write(names_string + "\n")  # Add a newline for formatting

        print(f"YAML file generated successfully at: {output_path}")

    except Exception as e:
        print(f"Error generating YAML file: {e}")

def filter_files_by_labels(data_dir, included_labels, image_extensions=(".jpg", ".jpeg", ".png"), label_extension=".txt"):
    """
    Filters files in the images and labels directories, keeping only those that contain
    any of the specified labels, and removes the original files.

    Args:
        data_dir (str): Path to the parent directory containing 'images' and 'labels' subdirectories.
        included_labels (list): A list of integers representing the labels to include.
                                 Files containing *any* of these labels will be kept.
        image_extensions (tuple, optional): Tuple of allowed image file extensions.
                                         Defaults to (".jpg", ".jpeg", ".png").
        label_extension (str, optional): The expected label file extension.
                                      Defaults to ".txt".
    """

    image_dir = os.path.join(data_dir, "images")
    label_dir = os.path.join(data_dir, "labels")

    if not os.path.exists(image_dir) or not os.path.exists(label_dir):
        print(f"Error: 'images' or 'labels' directory not found in {data_dir}")
        return

    # Collect files to keep
    images_to_keep = []
    labels_to_keep = []

    for filename in os.listdir(label_dir):
        if filename.endswith(label_extension):
            filepath = os.path.join(label_dir, filename)
            try:
                with open(filepath, "r") as f:
                    lines = f.readlines()

                file_contains_included_label = False
                for line in lines:
                    label = int(line.split()[0])

                    if label in included_labels:
                        file_contains_included_label = True
                        break
                if file_contains_included_label:
                    labels_to_keep.append(filename)
                    image_filename = os.path.splitext(filename)[0] + image_extensions[0]  # Assuming 1st extension
                    image_path = os.path.join(image_dir, image_filename)
                    if os.path.exists(image_path):
                        images_to_keep.append(image_filename)
                    else:
                        print(f"Warning: Corresponding image not found: {image_filename}")

            except Exception as e:
                print(f"Error processing file {filename}: {e}")

    # Remove files not in the keep lists
    for filename in os.listdir(image_dir):
        if filename not in images_to_keep and os.path.isfile(os.path.join(image_dir, filename)):
            try:
                os.remove(os.path.join(image_dir, filename))
                # print(f"Removed image: {filename}")
            except OSError as e:
                print(f"Error removing image {filename}: {e}")

    for filename in os.listdir(label_dir):
        if filename not in labels_to_keep and os.path.isfile(os.path.join(label_dir, filename)):
            try:
                os.remove(os.path.join(label_dir, filename))
                # print(f"Removed label: {filename}")
            except OSError as e:
                print(f"Error removing label {filename}: {e}")

    print("Filtering process completed.")
