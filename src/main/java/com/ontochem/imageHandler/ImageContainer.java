package com.ontochem.imageHandler;

import java.awt.image.BufferedImage;

/**
 * This class represents an image container that holds information about an image, its associated structure, and other relevant data.
 *
 * <h3>Changelog</h3>
 * <ul>
 *   <li>2023-06-14
 *     <ul>
 *       <li>Added support for image containers.</li>
 *     </ul>
 *   </li>
 * </ul>
 * 
 * @author shadrack.j.barnabas@ontochem.com
 */
public class ImageContainer {
    // Properties to store image-related information
    private String imageFile;           // Path to the image file
    private String structure;            // Structure associated with the image
    private String imageId;              // Identifier for the image
    private String reactionId;           // Identifier for the reaction
    private String serialId;             // Serial identifier
    private String userComment;          // User comment for the image
    private BufferedImage imageFromFile; // Image loaded from a file
    private BufferedImage imageFromStructure; // Image generated from the structure

    // Getters and setters for the properties
    public String getUserComment() {
        return userComment;
    }

    public void setUserComment(String userComment) {
        this.userComment = userComment;
    }

    public String getSerialId() {
        return serialId;
    }

    public void setSerialId(String serialId) {
        this.serialId = serialId;
    }

    public String getImageId() {
        return imageId;
    }

    public void setImageId(String imageId) {
        this.imageId = imageId;
    }

    public String getReactionId() {
        return reactionId;
    }

    public void setReactionId(String reactionId) {
        this.reactionId = reactionId;
    }

    public String getImageFile() {
        return imageFile;
    }

    public void setImageFile(String imageFile) {
        this.imageFile = imageFile;
    }

    public String getStructure() {
        return structure;
    }

    public void setStructure(String structure) {
        this.structure = structure;
    }

    public BufferedImage getImageFromFile() {
        return imageFromFile;
    }

    public void setImageFromFile(BufferedImage imageFromFile) {
        this.imageFromFile = imageFromFile;
    }

    public BufferedImage getImageFromStructure() {
        return imageFromStructure;
    }

    public void setImageFromStructure(BufferedImage imageFromStructure) {
        this.imageFromStructure = imageFromStructure;
    }
}
