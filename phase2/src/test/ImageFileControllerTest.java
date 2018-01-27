package test;

import controller.ImageFileController;
import model.ImageFile;
import junit.framework.TestCase;
import model.Tag;

import java.util.ArrayList;
import java.util.List;

public class ImageFileControllerTest extends TestCase {

  private List<ImageFile> allImageFilesList = new ArrayList<>();
  private ImageFileController imageFileController;
  private ImageFile imageFile1 = new ImageFile("tester.png");
  private ImageFile imageFile2 = new ImageFile("tester1.png");
  private ImageFile imageFile3 = new ImageFile("tester2.png");

  public void setUp() throws Exception {
    super.setUp();
    this.allImageFilesList.add(imageFile1);
    this.allImageFilesList.add(imageFile2);
    this.allImageFilesList.add(imageFile3);
    this.imageFileController = new ImageFileController(allImageFilesList);
  }

  /**
   * Testing the Correct Case whether ImageFileController has an ImageFile
   */
  public void testHasFileTestTrue() {
    String original = "tester";
    assertTrue(imageFileController.hasImageFile(new ImageFile(original + ".png")));
  }

  /**
   * Testing False Case whether ImageFileController has an ImageFile
   */
  public void testHasFileTestFalse() {
    assertFalse(imageFileController.hasImageFile(new ImageFile("invalid.png")));
  }

  /**
   * Testing get a Single Image File
   */
  public void testGetImageFileSingle() {
    ImageFile result = imageFile1;
    assertEquals(result, imageFileController.getImageFile(imageFile1));
  }

  /**
   * Testing to get Multiple ImageFiles
   */
  public void testGetImageFileMultiple() {
    ImageFile expect1 = imageFile1;
    ImageFile expect2 = imageFile2;
    ImageFile result1 = imageFileController.getImageFile(imageFile1);
    ImageFile result2 = imageFileController.getImageFile(imageFile2);
    assertEquals(expect1, result1);
    assertEquals(expect2, result2);
  }

  /**
   * Testing to add a Single ImageFile
   */
  public void testAddImageFileSingle() {
    ImageFile toAdd = new ImageFile("tester3.png");
    imageFileController.addImageFile(toAdd);
    assertTrue(imageFileController.hasImageFile(toAdd));
  }

  /**
   * Testing to add multiple ImagesFiles
   */
  public void testAddImageFileMultiple() {
    ImageFile toAdd1 = new ImageFile("tester3.png");
    ImageFile toAdd2 = new ImageFile("tester4.png");
    ArrayList<ImageFile> result = new ArrayList<>();
    imageFileController.addImageFile(toAdd1);
    imageFileController.addImageFile(toAdd2);
    result.add(imageFile1);
    result.add(imageFile2);
    result.add(imageFile3);
    result.add(toAdd1);
    result.add(toAdd2);
    assertTrue(imageFileController.hasImageFile(toAdd2));
    assertTrue(imageFileController.hasImageFile(toAdd1));
    assertEquals(result, imageFileController.getAllImageFilesList());
  }

  /**
   * Testing to add tags to a particular ImageFile
   */
  public void testAddTagsToImageFile() {

    String startingName = imageFile1.getOriginalName();
    Tag toAdd = new Tag("Faiyaz");
    imageFileController.addTagToImageFile(imageFile1, toAdd);
    List<Tag> result = imageFile1.getCurrentTags();
    List<Tag> expect = new ArrayList<>();
    expect.add(toAdd);
    assertEquals(expect, result);
    // test whether the name has been changed to reflect the addition of tag
    assertEquals(startingName + " " + toAdd.getTagName(), imageFile1.getImageName());
  }

  /**
   * Testing to Remove tags from a Particular ImageFile
   */
  public void testRemoveTagsFromImageFile() {

    ImageFile tester = new ImageFile("tester4.png");
    String startingName = tester.getOriginalName();
    Tag tagTester = new Tag("Faiyaz");
    Tag tagTester2 = new Tag("Muaz");
    tester.addTag(tagTester);
    tester.addTag(tagTester2);
    imageFileController.addImageFile(tester);
    imageFileController.removeTagFromFile(tester, tagTester);
    List<Tag> result = tester.getCurrentTags();
    List<Tag> expect = new ArrayList<>();
    expect.add(tagTester2);
    assertEquals(expect, result);
    // test whether the name has been changed to reflect the change in tags
    assertEquals(startingName + " " + tagTester2.getTagName(), tester.getImageName());
  }

  /**
   * Testing to Remove a particular Tag from all ImageFiles.
   */
  public void testRemoveTagFromAllImages() {

    //Creating Tags to be associated with Images
    Tag tagTester = new Tag("Faiyaz");
    Tag tagTester2 = new Tag("Muaz");

    //Creating Images and associating them to Tags
    ImageFile tester = new ImageFile("tester4.png");
    tester.addTag(tagTester);
    tester.addTag(tagTester2);
    ImageFile tester1 = new ImageFile("tester2.png");
    tester1.addTag(tagTester);
    tester1.addTag(tagTester2);
    ImageFile tester2 = new ImageFile("tester1.png");
    tester2.addTag(tagTester);
    tester2.addTag(tagTester2);

    //Adding all images to List and creating new ImageFileController
    List<ImageFile> images = new ArrayList<>();
    images.add(tester);
    images.add(tester1);
    images.add(tester2);
    ImageFileController imageFileController = new ImageFileController(images);

    //Calling method to test
    imageFileController.removeTagFromAllFiles(tagTester);

    //Testing expected versus actual result value
    List<Tag> result = tester.getCurrentTags();
    List<Tag> result1 = tester1.getCurrentTags();
    List<Tag> result2 = tester2.getCurrentTags();
    List<Tag> expect = new ArrayList<>();
    expect.add(tagTester2);
    assertEquals(expect, result);
    assertEquals(expect, result1);
    assertEquals(expect, result2);
  }


}
