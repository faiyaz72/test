package helperClasses;

import model.ImageFile;
import javafx.scene.control.ListCell;
import javafx.scene.image.ImageView;

import java.io.FileNotFoundException;

/* Class to fill list with thumbnails of imageFiles */
public class FillWithThumbnails extends ListCell<ImageFile> {

  /**
   * Constructs a FillWithThumbnails object with a ImageFile
   *
   * @param item the ImageFile which thumbnail to be constructed from.
   * @param empty indicates if ImageFile refers to an image or is empty.
   */
  @Override
  public void updateItem(ImageFile item, boolean empty) {
    super.updateItem(item, empty);
    if (empty) {
      setGraphic(null);
      setText(null);
    } else {
      try {
        ImageView imageView = item.getImage(50, 50);
        setGraphic(imageView);
        setText(item.getImageName());
      } catch (FileNotFoundException e) {
        e.printStackTrace();
      }
    }
  }
}

