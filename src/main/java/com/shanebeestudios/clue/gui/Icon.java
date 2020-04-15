package com.shanebeestudios.clue.gui;

import com.shanebeestudios.clue.ClueGame;

import javax.swing.*;
import java.awt.*;

@SuppressWarnings("unused")
public class Icon {

    public static ImageIcon CLUE_LOGO = getScaledIcon("images/clue-logo.png", "main logo", 128, 54);
    public static ImageIcon CLUE_DOCK = getScaledIcon("images/clue-dock.png", "main logo", 175, 54);

    private static ImageIcon createImageIcon(String path, String description) {
        java.net.URL imgURL = ClueGame.class.getClassLoader().getResource(path);
        if (imgURL != null) {
            return new ImageIcon(imgURL, description);
        } else {
            System.err.println("Couldn't find file: " + path);
            return null;
        }
    }

    private static ImageIcon getScaledIcon(String path, String description, int w, int h) {
        java.net.URL imgURL = ClueGame.class.getClassLoader().getResource(path);
        if (imgURL != null) {
            ImageIcon i = new ImageIcon(imgURL, description);
            Image image = i.getImage();
            Image newImage = image.getScaledInstance(w, h, Image.SCALE_SMOOTH);
            return new ImageIcon(newImage);
        } else {
            System.err.println("Couldn't find file: " + path);
            return null;
        }
    }

}
