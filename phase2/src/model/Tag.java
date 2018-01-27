package model;

import java.io.Serializable;
import java.util.Objects;
import java.util.Observable;

/* A single Tag object */
public class Tag extends Observable implements Serializable {

  // Name of the model.Tag
  private String tagName;

  // Symbol that should be shown before each model.Tag
  private static final String SYMBOL = "@";

  /**
   * Constructs a new model.Tag Object
   *
   * @param tagName Name of the model.Tag that should be created
   */
  public Tag(String tagName) {
    this.tagName = tagName;
  }

  /**
   * Changes the name of this tag to new String tag name toChange.
   *
   * @param toChange New name of the model.Tag
   */
  public void changeTagName(String toChange) {
    this.tagName = toChange;
  }

  /**
   * Returns the String tag name of this tag with Symbol @ in front.
   *
   * @return Get the name of the model.Tag with symbol
   */
  public String getTagName() {
    return SYMBOL + tagName;
  }

  /**
   * Returns the String tag name of this tag without any symbols attached.
   *
   * @return Name of the model.Tag without symbol
   */
  @Override
  public String toString() {
    return tagName;
  }

  /**
   * Returns true if obj is a Tag object and is equal to this tag.
   *
   * @param obj Object that needs to be compared
   * @return whether object is equivalent to this model.Tag Object
   */
  @Override
  public boolean equals(Object obj) {
    return obj instanceof Tag && Objects.equals(((Tag) obj).tagName, this.tagName);
  }
}
