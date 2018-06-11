package com.example.frank.jinding.Bean;

public class ImageBean {
  private String originArg;
  private String thumbArg;
  private String image;

  public ImageBean(String originArg, String thumbArg, String image) {
    this.originArg = originArg;
    this.thumbArg = thumbArg;
    this.image = image;
  }

  public String getOriginArg() {
    return originArg;
  }

  public void setOriginArg(String originArg) {
    this.originArg = originArg;
  }

  public String getThumbArg() {
    return thumbArg;
  }

  public void setThumbArg(String thumbArg) {
    this.thumbArg = thumbArg;
  }

  public String getImage() {
    return image;
  }

  public void setImage(String image) {
    this.image = image;
  }
}
