package test;

import model.ImageFile;
import model.Tag;
import controller.TagController;
import junit.framework.TestCase;

import java.util.ArrayList;
import java.util.List;

public class TagControllerTest extends TestCase {

  private TagController managerTester;
  private ArrayList<Tag> result;
  private ImageFile imageTester;

  public void setUp() throws Exception {
    super.setUp();
    this.managerTester = new TagController(new ArrayList<>());
    this.result = new ArrayList<>();
    // Dummy Image File, for purposes of testing
    this.imageTester = new ImageFile("tester2792.png");
  }

  /**
   * Testing single Tag Creation
   */
  public void testSingleCreateTag() throws Exception {
    managerTester.createTag("Faiyaz");
    result.add(new Tag("Faiyaz"));
    assertEquals(result, managerTester.getExistingTags());
  }

  /**
   * Testing multiple Tag Creation
   */
  public void testMultipleCreateTag() throws Exception {
    managerTester.createTag("Test1");
    managerTester.createTag("Test2");
    managerTester.createTag("Test3");
    result.add(new Tag("Test1"));
    result.add(new Tag("Test2"));
    result.add(new Tag("Test3"));
    assertEquals(result, managerTester.getExistingTags());
  }

  /**
   * Testing Duplicate Tag Creation
   */
  public void testDuplicateCreateTag() throws Exception {
    managerTester.createTag("Tester");
    managerTester.createTag("Tester");
    result.add(new Tag("Tester"));
    assertEquals(result, managerTester.getExistingTags());
  }

  /**
   * Testing Creating a Tag with a file associated with it
   */
  public void testCreateTagWithFile() throws Exception {
    managerTester.createTagWithFile("Tester2", imageTester);
    result.add(new Tag("Tester2"));
    assertEquals(result, managerTester.getExistingTags());
  }

  /**
   * Testing Creating a Tag with duplicated file association
   */
  public void testCreateTagWithFileMultipleFiles() throws Exception {
    managerTester.createTagWithFile("Tester1", imageTester);
    managerTester.createTagWithFile("Tester1", new ImageFile("test.png"));
    result.add(new Tag("Tester1"));
    assertEquals(result, managerTester.getExistingTags());
  }

  /**
   * Testing removing a file from a Tag
   */
  public void testRemoveFileFromTag() throws Exception {
    managerTester.createTagWithFile("Tester", imageTester);
    ImageFile imageTester1 = new ImageFile("tester3.png");
    managerTester.addFileToTag(managerTester.getTagWithName("Tester"), imageTester1);
    managerTester.removeFileFromTag(managerTester.getTagWithName("Tester"), imageTester);
    List<ImageFile> imageResult = managerTester
        .showFilesWithTag(managerTester.getTagWithName("Tester"));
    List<ImageFile> imageExpect = new ArrayList<>();
    imageExpect.add(imageTester1);
    assertEquals(imageExpect, imageResult);
  }

  /**
   * Testing removing multiple files from a Tag
   */
  public void testRemoveMultipleFilesFromTag() throws Exception {
    managerTester.createTagWithFile("Tester", imageTester);
    ImageFile imageTester1 = new ImageFile("tester3.png");
    ImageFile imageTester2 = new ImageFile("tester4.png");
    managerTester.addFileToTag(managerTester.getTagWithName("Tester"), imageTester1);
    managerTester.addFileToTag(managerTester.getTagWithName("Tester"), imageTester2);
    managerTester.removeFileFromTag(managerTester.getTagWithName("Tester"), imageTester1);
    managerTester.removeFileFromTag(managerTester.getTagWithName("Tester"), imageTester2);
    List<ImageFile> imageResult = managerTester
        .showFilesWithTag(managerTester.getTagWithName("Tester"));
    List<ImageFile> imageExpect = new ArrayList<>();
    imageExpect.add(imageTester);
    assertEquals(imageExpect, imageResult);
  }

  /**
   * Testing Renaming of an Existing Tag
   */
  public void testRenameTag() throws Exception {
    managerTester.createTag("Faiyaz");
    managerTester.createTag("Bob");
    managerTester.createTag("ManchesterUnited");
    managerTester.renameTag(managerTester.getTagWithName("Faiyaz"), "Liverpool");
    List<Tag> tagResult = managerTester.getExistingTags();
    result.add(new Tag("Liverpool"));
    result.add(new Tag("Bob"));
    result.add(new Tag("ManchesterUnited"));
    assertEquals(result, tagResult);
  }

  /**
   * Testing deleting a Single Existing Tag
   */
  public void testSingleDeleteTag() throws Exception {
    managerTester.createTag("Faiyaz");
    managerTester.createTag("Bob");
    managerTester.createTag("ManchesterUnited");
    managerTester.deleteTag(managerTester.getTagWithName("Faiyaz"));
    List<Tag> tagResult = managerTester.getExistingTags();
    result.add(new Tag("Bob"));
    result.add(new Tag("ManchesterUnited"));
    assertEquals(result, tagResult);
  }

  /**
   * Testing Deleting multiple Existing Tags
   */
  public void testMultipleDeleteTag() throws Exception {
    managerTester.createTag("Faiyaz");
    managerTester.createTag("Bob");
    managerTester.createTag("ManchesterUnited");
    managerTester.deleteTag(managerTester.getTagWithName("Faiyaz"));
    managerTester.deleteTag(managerTester.getTagWithName("ManchesterUnited"));
    List<Tag> tagResult = managerTester.getExistingTags();
    result.add(new Tag("Bob"));
    assertEquals(result, tagResult);
  }

  /**
   * Testing to Delete a Tag which does not exist
   */
  public void testInvalidDeleteTag() throws Exception {
    managerTester.createTag("Faiyaz");
    managerTester.createTag("Bob");
    managerTester.createTag("ManchesterUnited");
    managerTester.deleteTag(new Tag("Invalid"));
    List<Tag> tagResult = managerTester.getExistingTags();
    result.add(new Tag("Faiyaz"));
    result.add(new Tag("Bob"));
    result.add(new Tag("ManchesterUnited"));
    assertEquals(result, tagResult);
  }

  /**
   * Testing to display all files associated with a single Tag
   */
  public void testShowFilesWithSingleTag() throws Exception {
    managerTester.createTag("ManchesterUnited");
    managerTester.addFileToTag(managerTester.getTagWithName("ManchesterUnited"), imageTester);
    ImageFile imageTester1 = new ImageFile("tester3.png");
    managerTester.addFileToTag(managerTester.getTagWithName("ManchesterUnited"), imageTester1);
    ImageFile imageTester2 = new ImageFile("tester4.png");
    managerTester.addFileToTag(managerTester.getTagWithName("ManchesterUnited"), imageTester2);
    List<ImageFile> imageResult = managerTester.showFilesWithTag(managerTester.getTagWithName(
        "ManchesterUnited"));
    List<ImageFile> result = new ArrayList<>();
    result.add(imageTester);
    result.add(imageTester1);
    result.add(imageTester2);
    assertEquals(result, imageResult);

  }


}