package me.jgsb.recipejsongen;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.*;
import java.net.URL;
import java.util.Arrays;
import java.util.regex.Pattern;

public class Screen extends JPanel implements Runnable {

    private static final long serialVersionUID = 1L;

    private Thread thread = new Thread(this);

    private Frame frame;
    private JButton generate;
    private static Screen screen;
    private Image bgImage;

    public Screen(Frame frame){
        this.frame = frame;
        screen = this;
        this.setLayout(new FlowLayout(FlowLayout.CENTER));

        this.thread.start();

        JFileChooser fc = new JFileChooser();

        JPanel panel = new JPanel() {
            @Override
            public Dimension getPreferredSize() {
                return new Dimension(360, 60);
            }
        };

        generate = new JButton("Generate");
        generate.addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int returnVal = fc.showOpenDialog(screen);

                if(returnVal == JFileChooser.APPROVE_OPTION) {
                    try {
                        File file = fc.getSelectedFile();
                        String modid = JOptionPane.showInputDialog("What is your mod id for " + file.getName() + "?");
                        Reader reader = new FileReader(file);
                        PrintWriter writer;
                        BufferedReader br = new BufferedReader(reader);
                        String line;

                        while ((line = br.readLine()) != null) {
                            if (line.contains("GameRegistry")) {
                                if (line.contains("addRecipe")) {
                                    String parts[] = line.split(",");
                                    System.out.println(Arrays.toString(parts));
                                    String result = modid + ":" + parts[0].split(Pattern.quote("."))[2];
                                    System.out.println(result);
                                    writer = new PrintWriter(new File(file.getParent(), parts[0].split(Pattern.quote("."))[2] + ".json"), "UTF-8");
                                    String pattern = (parts[2] + (parts[3].contains("\"") ? ",\n" + parts[3] + (parts[4].contains("\"") ? ",\n" + parts[4] : "") : "")).replaceAll(" ", "").replaceAll("\n", "\n    ");
                                    String key = "  \"key\": {";
                                    int j = 0;
                                    for (int i = 0; i < parts.length; i++) {
                                        String part = parts[i];
                                        if (part.contains("'")) {
                                            String item = parts[i + 1];
                                            key += (j != 0 ? "," : "") + "\n    \"" + part.replaceAll("'", "").replaceAll(" ", "") + "\": {\n" +
                                                    "      \"item\": \"" + (item.contains(" Blocks.") || item.contains(" Items.") ? "minecraft:" : modid + ":") + item.split(Pattern.quote("."))[1].replaceAll(Pattern.quote(")"), "").replaceAll(";", "").toLowerCase() + "\"\n" +
                                                    "    }";
                                            j++;
                                        }
                                    }

                                    key += "\n  }";
                                    System.out.println(key);

                                    writer.write("{\n" +
                                            "  \"result\": {\n" +
                                            "    \"item\": \"" + result + "\"\n" +
                                            "  },\n" +
                                            "  \"pattern\": [\n" +
                                            "    " + pattern + "\n" +
                                            "  ],\n" +
                                            "  \"type\": \"minecraft:crafting_shaped\",\n" +
                                            key + "\n" +
                                            "}");
                                    writer.close();
                                } else if(line.contains("addShapelessRecipe")) {
                                    String parts[] = line.split(",");
                                    System.out.println(Arrays.toString(parts));
                                    String result = modid + ":" + parts[0].split(Pattern.quote("."))[2];
                                    System.out.println(result);
                                    writer = new PrintWriter(new File(file.getParent(), parts[0].split(Pattern.quote("."))[2] + ".json"), "UTF-8");
                                    String ingredients = "  \"ingredients\": [";
                                    int j = 0;
                                    for (int i = 2; i < parts.length; i++) {
                                        String part = parts[i];
                                        if (part.contains(".")) {
                                            ingredients += (j != 0 ? "," : "") + "\n" +
                                                    "    {\n" +
                                                    "      \"item\": \"" + (part.contains(" Blocks.") || part.contains(" Items.") ? "minecraft:" : modid + ":") + part.split(Pattern.quote("."))[1].replaceAll(Pattern.quote(")"), "").replaceAll(";", "").toLowerCase() + "\"\n" +
                                                    "    }";
                                            j++;
                                        }
                                    }

                                    ingredients += "\n  ]";
                                    System.out.println(ingredients);

                                    writer.write("{\n" +
                                            "  \"result\": {\n" +
                                            "    \"item\": \"" + result + "\"\n" +
                                            "  },\n" +
                                            "  \"type\": \"minecraft:crafting_shapeless\",\n" +
                                            ingredients + "\n" +
                                            "}");
                                    writer.close();
                                }
                            }
                        }
                    } catch (Exception e1) {
                        JOptionPane.showMessageDialog(screen, "An error has occurred");
                        e1.printStackTrace();
                    }
                }
            }

            @Override
            public void mousePressed(MouseEvent e) {

            }

            @Override
            public void mouseReleased(MouseEvent e) {

            }

            @Override
            public void mouseEntered(MouseEvent e) {

            }

            @Override
            public void mouseExited(MouseEvent e) {

            }
        });

        panel.setBackground(Color.white);
        panel.add(generate);
        frame.add(panel, BorderLayout.SOUTH);
    }

    public void paintComponent(Graphics g){
        g.clearRect(0, 0, Frame.WIDTH, Frame.HEIGHT);
        drawBG(g);
    }

    private void drawBG(Graphics g){
        if(this.bgImage == null) {
            try {
                URL imagePath = Screen.class.getResource("background.png");
                bgImage = Toolkit.getDefaultToolkit().getImage(imagePath);
            } catch(Exception e) {
                e.printStackTrace();
            }
        }

        g.drawImage(bgImage, 0, 0, 360, 300, this);
    }

    private long maxFrameRate = 60;

    public static void pause(int a){
        try {
            Thread.sleep(a);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void run() {
        long secondStart = System.currentTimeMillis();

        try {
            Thread.sleep(1000/this.maxFrameRate);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        repaint();

        if(secondStart + 1000 <= System.currentTimeMillis()){
            secondStart = System.currentTimeMillis();
        }
    }

}