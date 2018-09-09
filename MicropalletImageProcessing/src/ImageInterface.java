
import java.util.Scanner;
import java.io.*;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JColorChooser;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSlider;
import javax.swing.JSplitPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextField;
import javax.swing.UIManager;
import javax.swing.UIManager.LookAndFeelInfo;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.Graphics2D;
import java.awt.GraphicsConfiguration;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.Toolkit;

import javax.swing.JFrame;
import javax.swing.*;

/**
 * TODO add remove button functionality. Finish all sub component skeletons
 * 
 * @author relarix
 *
 */
public class ImageInterface {

	static GraphicsConfiguration gc;
	public static JFrame UIWindow;
	public static JPanel mainUI;
	public static JPanel fileChooserPanel;
	public static JPanel imagePrepPanel;
	public static JPanel imageViewPanel;
	public static JPanel inputOutputPanel;
	public static JPanel scanReqPanel;
	public static JTabbedPane tabbedPane;
	public static ArrayList<JButton> buttonArray = new ArrayList<JButton>();
	public static ArrayList<Color> colorArray = new ArrayList<Color>();
	public static ArrayList<JTextField> ChannelNameTextField = new ArrayList<JTextField>();
	public static ArrayList<String> ChannelNameList = new ArrayList<String>();
	public static ArrayList<JLabel> labelArray = new ArrayList<JLabel>();
	public static ArrayList<JButton> removeButtonArray = new ArrayList<JButton>();
	public static ArrayList<Channel> channelList = new ArrayList<Channel>();
	public static ArrayList<PixelRange> PixelRangeList = new ArrayList<PixelRange>();
	public static ArrayList<JCheckBox> selectBoxArray = new ArrayList<JCheckBox>();
	public static boolean[][][][][] motherMatrix;
	public static Picture DisplayPic;
	public static JLabel numChannels;
	public static JButton addChannelButton;
	public static JLabel numChannelsLabel;
	public static int numChannelInquiry = 1;
	public static int startingRow = 0;
	public static int startingCol = 0;
	public static int endingRow = 0;
	public static int endingCol = 0;
	public static int wellSize = 0;
	public static int borderSize = 0;
	public static int gridDimension = 0;
	public static double pixelFraction = 0;
	public static int numCellFound = 0;
	public static JPanel imageEditPanel;
	public static String hintIconPath = "src/resources/images/hint.jpg";
	public static void main(String[] args) throws ClassNotFoundException, InstantiationException, IllegalAccessException, UnsupportedLookAndFeelException {
		try {
		
		    for (LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
		       if ("Nimbus".equals(info.getName())) {
		           UIManager.setLookAndFeel(info.getClassName());
		           break;
		        }
		    }
		} catch (Exception e) {
		  
		}
		loadFrame();

		loadMainUI();

		loadFileChooserPanel();
		loadImagePrepPanel();
		loadImageViewPanel();
		loadScanReqPanel();
		loadInputOutputPanel();

		setUpFileChooserPanel();
		setUpImagePrepPanel();
		setUpScanReqPanel();
		setUpInputOutputPanel();

		loadPanels();
	}

	public static void setUpFileChooserPanel() {
		JLabel ImageLabel = new JLabel();

		imageViewPanel.add(ImageLabel);

		addChannelButton = new JButton("Add Channel");
		numChannels = new JLabel(0 + "");
		numChannelsLabel = new JLabel("Number of Channels: " + channelList.size());

		// adding a new channel object
		addChannelButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {

				numChannels.setText(channelList.size() + 1 + "");
				Channel tempChannel = new Channel(Integer.parseInt(numChannels.getText()), numChannelsLabel,
						fileChooserPanel, channelList, imageViewPanel);
				tempChannel.addChannel();

				channelList.add(tempChannel);
				numChannelsLabel.setText("Number of Channels: " + channelList.size());
				
				fileChooserPanel.revalidate();
				fileChooserPanel.repaint();
				
				setUpInputOutputPanel();
			}
		});

		JButton ClearFileChooserButton = new JButton("Clear");
		ClearFileChooserButton.addMouseListener(new java.awt.event.MouseAdapter() {
		    public void mouseEntered(java.awt.event.MouseEvent evt) {
		    	ClearFileChooserButton.setBackground(Color.RED);
		    }

		    public void mouseExited(java.awt.event.MouseEvent evt) {
		    	ClearFileChooserButton.setBackground(UIManager.getColor("control"));
		    }
		});
			
		ClearFileChooserButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				System.out.println("<<Channel Names Cleared>>");
				for (int i = 0; i < channelList.size(); i++) {
					channelList.get(i).clearName();
				}
				System.out.println();
			}
		});
		
		JButton SaveFileChooserButton = new JButton("Save Channel Names");
		SaveFileChooserButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				System.out.println("<<Channel Names Saved>>");
				for (int i = 0; i < channelList.size(); i++) {
					ChannelNameList.add(i, channelList.get(i).getName());
					System.out.println("Channel #" + (i + 1) + ": " + channelList.get(i).getName());
				}
				setUpScanReqPanel();
				scanReqPanel.revalidate();
				scanReqPanel.repaint();
				System.out.println();
			}
		});

		JLabel SpaceFill = new JLabel("");

		fileChooserPanel.add(addChannelButton);
		fileChooserPanel.add(numChannelsLabel);
		skip1Col(fileChooserPanel);
		fileChooserPanel.add(ClearFileChooserButton);
		fileChooserPanel.add(SaveFileChooserButton);
		fileChooserPanel.add(SpaceFill);
	}
	
	public static Image getScaledImage(Image srcImg, int w, int h){
	    BufferedImage resizedImg = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
	    Graphics2D g2 = resizedImg.createGraphics();

	    g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
	    g2.drawImage(srcImg, 0, 0, w, h, null);
	    g2.dispose();

	    return resizedImg;
	}
	
	public static void setUpImagePrepPanel() {
		ArrayList<JButton> tempButtonArray=new ArrayList<JButton>();
		
		JLabel StartingRow = new JLabel("Starting Row: ");
		ImageIcon hintIcon= new ImageIcon(hintIconPath);
		Image hintImage=hintIcon.getImage();
		Image scaledHint=hintImage.getScaledInstance(40, 40,  java.awt.Image.SCALE_SMOOTH);
		
		hintIcon=new ImageIcon(scaledHint);
		JButton StartingRowExp = new JButton("", hintIcon);
		tempButtonArray.add(StartingRowExp);
		StartingRowExp.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				JOptionPane.showMessageDialog(null,
						"The row number of the pixel that is located on the top left corner of the most top left well of interest. \n Make sure to leave enough space when choosing the top left well of interest. \n (Essentially, the user is drawing a line from the top left corner of the desired top row to the top right corner of the desired top row so the software can adjust for slight tilt of the entire slide image.)");
			}
		});
		JTextField StartingRowInput = new JTextField("155");
		JLabel StartingCol = new JLabel("Starting Column: ");
		JButton StartingColExp = new JButton("", hintIcon);
		tempButtonArray.add(StartingColExp);
		StartingColExp.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				JOptionPane.showMessageDialog(null,
						"The column number of the pixel that is located on the top left corner of the most top left well of interest. \n (Essentially, the user is drawing a line from the top left corner of the desired top row to the top right corner of the desired top row so the software can adjust for slight tilt of the entire slide image.)");
			}
		});
		JTextField StartingColInput = new JTextField("154");
		JLabel EndingRow = new JLabel("Ending Row: ");
		JButton EndingRowExp = new JButton("", hintIcon);
		tempButtonArray.add(EndingRowExp);
		EndingRowExp.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				JOptionPane.showMessageDialog(null,
						"The row number of the pixel that is located on the top right corner of the most top right well of interest. \n (Essentially, the user is drawing a line from the top left corner of the desired top row to the top right corner of the desired top row so the software can adjust for slight tilt of the entire slide image.)");
			}
		});
		JTextField EndingRowInput = new JTextField("65");
		JLabel EndingCol = new JLabel("Ending Column: ");
		JButton EndingColExp = new JButton("", hintIcon);
		tempButtonArray.add(EndingColExp);
		EndingColExp.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				JOptionPane.showMessageDialog(null,
						"The column number of the pixel that is located on the top right corner of the most top right well of interest. \n (Essentially, the user is drawing a line from the top left corner of the desired top row to the top right corner of the desired top row so the software can adjust for slight tilt of the entire slide image.)");
			}
		});
		JTextField EndingColInput = new JTextField("8600");
		JLabel WellSize = new JLabel("Well Size: ");
		JButton WellSizeExp = new JButton("", hintIcon);
		tempButtonArray.add(WellSizeExp);
		WellSizeExp.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				JOptionPane.showMessageDialog(null, "The size of the well in between the borders/streets in pixels.");
			}
		});
		JTextField WellSizeInput = new JTextField("20");
		JLabel BorderSize = new JLabel("Border Size: ");
		JButton BorderSizeExp = new JButton("", hintIcon);
		tempButtonArray.add(BorderSizeExp);
		BorderSizeExp.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				JOptionPane.showMessageDialog(null, "The size of the border in between wells in pixels.");
			}
		});
		JTextField BorderSizeInput = new JTextField("10");
		JLabel GridDimension = new JLabel("Grid Dimension: ");
		JButton GridDimensionExp = new JButton("", hintIcon);
		tempButtonArray.add(GridDimensionExp);
		GridDimensionExp.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				JOptionPane.showMessageDialog(null,
						"To provide some degrees of convenience, the software will draw a larger gird with every block consisting x by x wells. Please enter the value of x. (x must be greater than 8)");
			}
		});
		JTextField GridDimensionInput = new JTextField("16");
		JLabel PixelFraction = new JLabel("Pixel Fraction: ");
		JButton PixelFractionExp = new JButton("", hintIcon);
		tempButtonArray.add(PixelFractionExp);
		PixelFractionExp.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				JOptionPane.showMessageDialog(null,
						"Since the Border Size + Well Size in whole number of pixels is not accurate enough to be used across the entire image, slide the slider to adjust the fraction of pixel in addition to the sum of Border Size and Well Size.");
			}
		});
		JSlider PixelFractionInputSlider = new JSlider(0,100,65);
		PixelFractionInputSlider.setMajorTickSpacing(5);
		PixelFractionInputSlider.setPaintTicks(true);
		
		for (JButton jb: tempButtonArray)
		{
			//jb.setOpaque(false);
			//jb.setContentAreaFilled(false);
			//jb.setBorderPainted(false);
			//jb.setFocusPainted(false);
			jb.addMouseListener(new java.awt.event.MouseAdapter() {
			    public void mouseEntered(java.awt.event.MouseEvent evt) {
			        jb.setBackground(Color.BLUE);
			    }

			    public void mouseExited(java.awt.event.MouseEvent evt) {
			        jb.setBackground(UIManager.getColor("control"));
			    }
			});
		}
	    
		 java.util.Hashtable<Integer,JLabel> labelTable = new java.util.Hashtable<Integer,JLabel>();
		    labelTable.put(new Integer(100), new JLabel("1.0"));
		    labelTable.put(new Integer(50), new JLabel("0.5"));
		    
		    labelTable.put(new Integer(0), new JLabel("0.0"));
		    PixelFractionInputSlider.setLabelTable( labelTable );
		    PixelFractionInputSlider.setPaintLabels(true);
//		    
//		    PixelFractionInputSlider.addChangeListener(new javax.swing.event.ChangeListener(){
//		        public void stateChanged(javax.swing.event.ChangeEvent ce){
//		    		System.out.println("<<Saved Parameters>>");
//					startingRow = Integer.parseInt(StartingRowInput.getText());
//					System.out.println("Starting Row: " + startingRow);
//					startingCol = Integer.parseInt(StartingColInput.getText());
//					System.out.println("Starting Column: " + startingCol);
//					endingRow = Integer.parseInt(EndingRowInput.getText());
//					System.out.println("Ending Row: " + endingRow);
//					endingCol = Integer.parseInt(EndingColInput.getText());
//					System.out.println("Ending Column: " + endingCol);
//					wellSize = Integer.parseInt(WellSizeInput.getText());
//					System.out.println("Well Size: " + wellSize);
//					borderSize = Integer.parseInt(BorderSizeInput.getText());
//					System.out.println("Border Size: " + borderSize);
//					gridDimension = Integer.parseInt(GridDimensionInput.getText());
//					System.out.println("Grid Dimension: " + gridDimension);
//					pixelFraction = PixelFractionInputSlider.getValue()/100.0;
//					System.out.println("Pixel Fraction: " + pixelFraction);
//					System.out.println();
//					Picture picture1 = new Picture(channelList.get(0).getFilePath());
//					picture1.drawFrame(startingRow, startingCol, endingRow, endingCol, wellSize, borderSize, gridDimension,
//							pixelFraction);
//					imageViewPanel.removeAll();
//					PictureExplorer exp = new PictureExplorer(picture1, imageViewPanel);
//					imageViewPanel.revalidate();
//					imageViewPanel.repaint();
//		        }
//		      });
		    //make a new draw frame method that delivers a transparent picture including 
		    
		JButton ClearButton = new JButton("Clear");
		ClearButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				System.out.println("<<Parameters Cleared>>");
				StartingRowInput.setText("");
				StartingColInput.setText("");
				EndingRowInput.setText("");
				EndingColInput.setText("");
				WellSizeInput.setText("");
				BorderSizeInput.setText("");
				GridDimensionInput.setText("");
				PixelFractionInputSlider.setValue(65);
				System.out.println();
			}
		});

		JButton SaveButton = new JButton("Save and Try Parameters");
		SaveButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				System.out.println("<<Saved Parameters>>");
				startingRow = Integer.parseInt(StartingRowInput.getText());
				System.out.println("Starting Row: " + startingRow);
				startingCol = Integer.parseInt(StartingColInput.getText());
				System.out.println("Starting Column: " + startingCol);
				endingRow = Integer.parseInt(EndingRowInput.getText());
				System.out.println("Ending Row: " + endingRow);
				endingCol = Integer.parseInt(EndingColInput.getText());
				System.out.println("Ending Column: " + endingCol);
				wellSize = Integer.parseInt(WellSizeInput.getText());
				System.out.println("Well Size: " + wellSize);
				borderSize = Integer.parseInt(BorderSizeInput.getText());
				System.out.println("Border Size: " + borderSize);
				gridDimension = Integer.parseInt(GridDimensionInput.getText());
				System.out.println("Grid Dimension: " + gridDimension);
				pixelFraction = PixelFractionInputSlider.getValue()/100.0;
				System.out.println("Pixel Fraction: " + pixelFraction);
				System.out.println();
				Picture picture1 = new Picture(channelList.get(0).getFilePath());
				picture1.drawFrame(startingRow, startingCol, endingRow, endingCol, wellSize, borderSize, gridDimension,
						pixelFraction);
				imageViewPanel.removeAll();
				PictureExplorer exp = new PictureExplorer(picture1, imageViewPanel);
				imageViewPanel.revalidate();
				imageViewPanel.repaint();
				//tabbedPane.remove(1);
				//tabbedPane.addTab("Image Viewer", null, imageViewPanel, "Locating image coordinates");

			}
		});

		JButton ComfirmButton = new JButton("Fix Param & Grid Image");
		ComfirmButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				System.out.println("<<Comfirmed Parameters and Gridded>>");
				startingRow = Integer.parseInt(StartingRowInput.getText());
				System.out.println("Starting Row: " + startingRow);
				startingCol = Integer.parseInt(StartingColInput.getText());
				System.out.println("Starting Column: " + startingCol);
				endingRow = Integer.parseInt(EndingRowInput.getText());
				System.out.println("Ending Row: " + endingRow);
				endingCol = Integer.parseInt(EndingColInput.getText());
				System.out.println("Ending Column: " + endingCol);
				wellSize = Integer.parseInt(WellSizeInput.getText());
				System.out.println("Well Size: " + wellSize);
				borderSize = Integer.parseInt(BorderSizeInput.getText());
				System.out.println("Border Size: " + borderSize);
				gridDimension = Integer.parseInt(GridDimensionInput.getText());
				System.out.println("Grid Dimension: " + gridDimension);
				pixelFraction = PixelFractionInputSlider.getValue()/100.0;
				System.out.println("Pixel Fraction: " + pixelFraction);
				System.out.println();
				Picture picture1 = new Picture(channelList.get(0).getFilePath());
				picture1.drawFrame(startingRow, startingCol, endingRow, endingCol, wellSize, borderSize, gridDimension, pixelFraction);
				imageViewPanel.removeAll();
				PictureExplorer exp=new PictureExplorer(picture1, imageViewPanel);
				imageViewPanel.revalidate();
				imageViewPanel.repaint();
				tabbedPane.remove(1);
				tabbedPane.addTab("Image Viewer", null, imageViewPanel, "Locating image coordinates");
				
				for(int i = 0; i < channelList.size(); i++)
				{
					Picture picture2 = new Picture(channelList.get(i).getFilePath());
					picture2.drawFrame(startingRow, startingCol, endingRow, endingCol, wellSize, borderSize, gridDimension, pixelFraction);
					String name = "Channel #" + (i+1) + " " + channelList.get(i).getName() + " - Edit.jpg";
					picture2.write(name);
				}
				picture1 = channelList.get(0).getContrastImageFile();
				imageViewPanel.removeAll();
				exp = new PictureExplorer(picture1, imageViewPanel);
				imageViewPanel.revalidate();
				imageViewPanel.repaint();
			}
		});

		imagePrepPanel.add(StartingRow);
		imagePrepPanel.add(StartingRowExp);
		imagePrepPanel.add(StartingRowInput);
		imagePrepPanel.add(StartingCol);
		imagePrepPanel.add(StartingColExp);
		imagePrepPanel.add(StartingColInput);
		imagePrepPanel.add(EndingRow);
		imagePrepPanel.add(EndingRowExp);
		imagePrepPanel.add(EndingRowInput);
		imagePrepPanel.add(EndingCol);
		imagePrepPanel.add(EndingColExp);
		imagePrepPanel.add(EndingColInput);
		imagePrepPanel.add(WellSize);
		imagePrepPanel.add(WellSizeExp);
		imagePrepPanel.add(WellSizeInput);
		imagePrepPanel.add(BorderSize);
		imagePrepPanel.add(BorderSizeExp);
		imagePrepPanel.add(BorderSizeInput);
		imagePrepPanel.add(GridDimension);
		imagePrepPanel.add(GridDimensionExp);
		imagePrepPanel.add(GridDimensionInput);
		imagePrepPanel.add(PixelFraction);
		imagePrepPanel.add(PixelFractionExp);
		imagePrepPanel.add(PixelFractionInputSlider);
		imagePrepPanel.add(ClearButton);
		imagePrepPanel.add(SaveButton);
		imagePrepPanel.add(ComfirmButton);
	}
	
	public static void setUpScanReqPanel() {
		GridBagConstraints c = new GridBagConstraints();
		c.fill = GridBagConstraints.HORIZONTAL;
		JLabel ChannelName;
		c.gridx = 0;
		c.gridy = 0;
		c.ipady = 10;
		if (channelList.size() != 0)
			ChannelName = new JLabel(
					"Channel #" + numChannelInquiry + ": " + channelList.get(numChannelInquiry - 1).getName());
		else
			ChannelName = new JLabel("Channel #" + numChannelInquiry + ": ");
		scanReqPanel.add(ChannelName, c);

		JButton pickColor = new JButton("Pick a Color Threshold for the selected Channel");
		c.gridx = 0;
		c.gridy = 1;
		c.ipady = 32;
		scanReqPanel.add(pickColor, c);

		JLabel ColorValue = new JLabel();
		c.gridx = 0;
		c.gridy = 2;
		c.ipady = 32;
		scanReqPanel.add(ColorValue, c);

		pickColor.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				JPanel colorChooser = new JPanel();
				Color color = Color.white;
				color = JColorChooser.showDialog(null, "Pick a color", color);

				if (colorArray.size() < numChannelInquiry)
					colorArray.add(color);
				else
					colorArray.set(numChannelInquiry - 1, color);
				ColorValue.setText("Selected Color:   Red Value: " + color.getRed() + "   Green Value: "
						+ color.getGreen() + "   Blue: " + color.getBlue());
				scanReqPanel.revalidate();
				scanReqPanel.repaint();
			}
		});

		JLabel pickRange = new JLabel("<<Pick a Pixel Range for the selected Channel>>");
		c.gridx = 0;
		c.gridy = 3;
		c.ipady = 32;
		scanReqPanel.add(pickRange, c);

		JLabel rangeInstruction1 = new JLabel("The Pixel Range should be from (Lower Number)");
		c.gridx = 0;
		c.gridy = 4;
		c.ipady = 32;
		scanReqPanel.add(rangeInstruction1, c);

		JTextField lowerRange = new JTextField();
		c.gridx = 1;
		c.gridy = 4;
		c.ipady = 32;
		c.ipadx = 250;
		scanReqPanel.add(lowerRange, c);

		JLabel rangeInstruction2 = new JLabel(" to (Higher Number)");
		c.gridx = 0;
		c.gridy = 5;
		c.ipady = 32;
		c.ipadx = 0;
		scanReqPanel.add(rangeInstruction2, c);

		JTextField upperRange = new JTextField();
		c.gridx = 1;
		c.gridy = 5;
		c.ipady = 32;
		c.ipadx = 250;
		scanReqPanel.add(upperRange, c);

		JButton saveRange = new JButton("Save Pixel Range");
		c.gridx = 0;
		c.gridy = 6;
		c.ipady = 32;
		c.ipadx = 0;
		scanReqPanel.add(saveRange, c);

		JLabel RangeValue = new JLabel();
		c.gridx = 1;
		c.gridy = 6;
		c.ipady = 32;
		c.ipadx = 0;
		scanReqPanel.add(RangeValue, c);

		saveRange.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				PixelRange range = new PixelRange(Integer.parseInt(lowerRange.getText()),
						Integer.parseInt(upperRange.getText()));
				if (PixelRangeList.size() < numChannelInquiry)
					PixelRangeList.add(range);
				else
					PixelRangeList.set(numChannelInquiry - 1, range);
				RangeValue
						.setText("The Pixel Range is saved from " + PixelRangeList.get(numChannelInquiry - 1).getLower()
								+ " to " + PixelRangeList.get(numChannelInquiry - 1).getUpper());
				scanReqPanel.revalidate();
				scanReqPanel.repaint();
			}

		});

		JButton confirmColorRange = new JButton("Confirm and Continue");
		c.gridx = 0;
		c.gridy = 7;
		c.ipady = 32;
		c.ipadx = 0;
		scanReqPanel.add(confirmColorRange, c);

		confirmColorRange.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				PixelRange range = new PixelRange(Integer.parseInt(lowerRange.getText()),
						Integer.parseInt(upperRange.getText()));
				if (PixelRangeList.size() < numChannelInquiry)
					PixelRangeList.add(range);
				else
					PixelRangeList.set(numChannelInquiry - 1, range);
				RangeValue
						.setText("The Pixel Range is saved from " + PixelRangeList.get(numChannelInquiry - 1).getLower()
								+ " to " + PixelRangeList.get(numChannelInquiry - 1).getUpper());
				
				if(numChannelInquiry < channelList.size()){
				numChannelInquiry++;
				/*if(channelList.size() < numChannelInquiry){
					ChannelName.setText("");
					ChannelName.setText("Channel #" + numChannelInquiry + ": " + channelList.get(numChannelInquiry - 1).getName());
			}
				else {
					ChannelName.setText("");
					ChannelName.setText("Channel #" + numChannelInquiry + ": ");
				}
				ColorValue.setText("");
				lowerRange.setText("");
				upperRange.setText("");
				RangeValue.setText("");*/
				scanReqPanel.removeAll();
				setUpScanReqPanel();
				scanReqPanel.revalidate();
				scanReqPanel.repaint();
				storeInfo(channelList.get(numChannelInquiry - 2).getContrastImageFile().imageProcessing(startingRow, startingCol, endingRow, endingCol, wellSize, borderSize, gridDimension, pixelFraction, colorArray.get(numChannelInquiry - 2).getRed(), colorArray.get(numChannelInquiry - 2).getGreen(), colorArray.get(numChannelInquiry - 2).getBlue(), PixelRangeList.get(numChannelInquiry - 2).getLower(), PixelRangeList.get(numChannelInquiry - 2).getUpper()), numChannelInquiry - 2);
				//System.out.println("" + startingRow + " " +  startingCol + " " +  endingRow + " " +  endingCol + " " +  wellSize + " " +  borderSize + " " +  gridDimension + " " +  pixelFraction + " " +  colorArray.get(numChannelInquiry - 2).getRed() + " " +   colorArray.get(numChannelInquiry - 2).getGreen() + " " +  colorArray.get(numChannelInquiry - 2).getBlue() + " " +   PixelRangeList.get(numChannelInquiry - 2).getLower() + " " +  PixelRangeList.get(numChannelInquiry - 2).getUpper());
				Picture picture1 = channelList.get(numChannelInquiry - 1).getContrastImageFile();
				imageViewPanel.removeAll();
				PictureExplorer exp = new PictureExplorer(picture1, imageViewPanel);
				imageViewPanel.revalidate();
				imageViewPanel.repaint();
				}
				else {
					numChannelInquiry++;
					storeInfo(channelList.get(numChannelInquiry - 2).getContrastImageFile().imageProcessing(startingRow, startingCol, endingRow, endingCol, wellSize, borderSize, gridDimension, pixelFraction, colorArray.get(numChannelInquiry - 2).getRed(), colorArray.get(numChannelInquiry - 2).getGreen(), colorArray.get(numChannelInquiry - 2).getBlue(), PixelRangeList.get(numChannelInquiry - 2).getLower(), PixelRangeList.get(numChannelInquiry - 2).getUpper()), numChannelInquiry - 2);
					setUpInputOutputPanel();
					mainUI.revalidate();
					mainUI.repaint();
				}
			}

		});	}

	public static void setUpInputOutputPanel() {
		inputOutputPanel.removeAll();
		while(selectBoxArray.size() > 0)
		{
			selectBoxArray.remove(selectBoxArray.size() - 1);
		}
		JLabel numLabel = new JLabel("Number of Channels: " + channelList.size());
		inputOutputPanel.add(numLabel);
		skip1Col(inputOutputPanel);
		for(int i = 0; i < channelList.size(); i++)
		{
			JLabel channelRepName = new JLabel("Channel #" + (i+1) + ": " + channelList.get(i).getName());
			JCheckBox selectbox = new JCheckBox("Selected for Search");
			selectBoxArray.add(selectbox);
			inputOutputPanel.add(channelRepName);
			inputOutputPanel.add(selectbox);
		}
		JButton search = new JButton("Search");
		
		inputOutputPanel.add(search);
		JLabel numCell = new JLabel("Final Cell Count: " + numCellFound);
		inputOutputPanel.add(numCell);
		
		search.addActionListener(new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent e) {
						int count = 0;
						for(int i = 0; i < selectBoxArray.size(); i++)
						{
						if(selectBoxArray.get(i).getSelectedObjects() != null)
								count++;
						}
						int[] askArray = new int[count];
						count = 0;
						for(int i = 0; i < selectBoxArray.size(); i++)
						{
							if(selectBoxArray.get(i).getSelectedObjects() != null)
							{
								askArray[count] = i;
								count++;
							}
						}
						print(askArray);
						numCell.setText("Final Cell Count: " + numCellFound);
						inputOutputPanel.revalidate();
						inputOutputPanel.repaint();
					}
				});

		inputOutputPanel.revalidate();
		inputOutputPanel.repaint();
	}
		
		
	public static void loadPanels() {

		JScrollPane jsp = new JScrollPane(fileChooserPanel);

		mainUI.add(jsp);

		imageEditPanel = new JPanel();
		imageEditPanel.setLayout(new GridLayout(0, 1));
		JScrollPane jsp3 = new JScrollPane(scanReqPanel);
		
		imagePrepPanel.setMinimumSize(new Dimension(300, 0));
		JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, imageViewPanel,imagePrepPanel);
		splitPane.resetToPreferredSizes();
		splitPane.setOneTouchExpandable(true);
		
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();

		int scrW = (int)screenSize.getWidth();

		splitPane.setDividerLocation((int)(scrW*6/8.0));
		
		imageEditPanel.add(splitPane);
		
		mainUI.add(jsp3);
		mainUI.add(inputOutputPanel);
		tabbedPane = new JTabbedPane();
		tabbedPane.addTab("Inputs", null, mainUI, "File and inputs");
		tabbedPane.addTab("Image Viewer", null, imageEditPanel, "Locating image coordinates");

		UIWindow.add(tabbedPane);
		
		UIWindow.setVisible(true);
	}

	public static void loadFrame() {
		
		UIWindow = new JFrame(gc);
		UIWindow.setTitle("Image Processing Tool");
		UIWindow.setExtendedState(Frame.MAXIMIZED_BOTH);

	}

	public static void loadMainUI() {
		mainUI = new JPanel();
		mainUI.setLayout(new GridLayout(0, 2));
	}

	public static void loadFileChooserPanel() {
		fileChooserPanel = new JPanel();
		fileChooserPanel.setBorder(new TitledBorder(new EtchedBorder(), "File Selection"));
		fileChooserPanel.setLayout(new GridLayout(0, 3));
	}

	public static void loadInputOutputPanel() {
		inputOutputPanel = new JPanel();
		inputOutputPanel.setBorder(new TitledBorder(new EtchedBorder(), "Color Combination Search"));
		inputOutputPanel.setLayout(new GridLayout(0, 2));
	}

	public static void loadImageViewPanel() {
		imageViewPanel = new JPanel();
		imageViewPanel.setBorder(new TitledBorder(new EtchedBorder(), "Image Viewer"));
		imageViewPanel.setLayout(new BorderLayout());
	}

	public static void loadImagePrepPanel() {
		imagePrepPanel = new JPanel();
		imagePrepPanel.setBorder(new TitledBorder(new EtchedBorder(), "Image Grid Preparation"));
		imagePrepPanel.setLayout(new GridLayout(0, 3));
	}

	public static void loadScanReqPanel() {
		scanReqPanel = new JPanel();
		scanReqPanel.setBorder(new TitledBorder(new EtchedBorder(), "Scanning Parameters"));
		scanReqPanel.setLayout(new GridBagLayout());
	}

	public static void skip3Col(JPanel p) {
		JLabel EmptySpace = new JLabel("");
		JLabel EmptySpace1 = new JLabel("");
		JLabel EmptySpace2 = new JLabel("");
		p.add(EmptySpace);
		p.add(EmptySpace1);
		p.add(EmptySpace2);
	}
	
	public static void skip2Col(JPanel p) {
		JLabel EmptySpace = new JLabel("");
		JLabel EmptySpace1 = new JLabel("");
		p.add(EmptySpace);
		p.add(EmptySpace1);
	}
		
	public static void skip1Col(JPanel p) {
		JLabel EmptySpace = new JLabel("");
		p.add(EmptySpace);
	}

	public static void deleteChannel(Channel c) {
		channelList.remove(c);
	}

	public static void drawPicture(String f) throws IOException {
		System.out.println(f);
		imageViewPanel.removeAll();
		PictureExplorer exp = new PictureExplorer(new Picture(f), imageViewPanel);

	}

	public static void drawPicture(Picture p) throws IOException {
		PictureExplorer exp = new PictureExplorer(p, imageViewPanel);
		imageViewPanel.revalidate();
		imageViewPanel.repaint();
	}

	public static void storeInfo(boolean[][][][] array, int level) {
		if (level == 0) {
			motherMatrix = new boolean[array.length][array[0].length][array[0][0].length][array[0][0][0].length][10];
			for (int rr = 0; rr < array.length; rr++) {
				for (int cc = 0; cc < array[0].length; cc++) {
					for (int r = 0; r < array[0][0].length; r++) {
						for (int c = 0; c < array[0][0][0].length; c++) {
							motherMatrix[rr][cc][r][c][level] = false;
						}
					}
				}
			}
		}

		for (int rr = 0; rr < array.length; rr++) {
			for (int cc = 0; cc < array[0].length; cc++) {
				for (int r = 0; r < array[0][0].length; r++) {
					for (int c = 0; c < array[0][0][0].length; c++) {
						motherMatrix[rr][cc][r][c][level] = array[rr][cc][r][c];
						
					}
				}
			}
		}

	}

	/*public static void searchImageProcessing() {
		// ---Start Asking---//
		Scanner Scan = new Scanner(System.in);
		int[] inquiry;
		System.out.println("How many colors are in your combination of interest?");
		int num = Scan.nextInt();
		inquiry = new int[num];
		System.out.println("Enter all the numbers of interest with space in-between");
		for (int i = 0; i < num; i++) {
			inquiry[i] = Scan.nextInt() - 1;
		}

		print(inquiry); //0 1

	}*/

	private static void print(int[] inquiry) {
		int count = 0;
		String FileName = "Color Combination- ";
		boolean[][] displayMatrix = new boolean[motherMatrix[0][0].length][motherMatrix[0][0][0].length];

		for (int i = 0; i < inquiry.length; i++) {
			FileName = FileName + (inquiry[i] + 1) + " ";
		}
		String TextFileName = FileName + ".docx";
		String PicFileName = FileName + ".jpg";
		boolean newline;
		
		try
		{
			FileWriter table = new FileWriter(TextFileName);
			String CoreFile = "2016.04.";
			String Temp = CoreFile + "-DP.jpg";
			DisplayPic = new Picture(Temp);
			DisplayPic.drawFrame(startingRow, startingCol, endingRow, endingCol, wellSize, borderSize, gridDimension,
					pixelFraction);
			//look for booleans
			//System.out.println(motherMatrix.length + " " + motherMatrix[0].length + " " + motherMatrix[0][0].length + motherMatrix[0][0][0].length); 7/17/16/16
			for(int rr = 0; rr < motherMatrix.length; rr++)
			{
				for(int cc = 0; cc < motherMatrix[0].length; cc++)
				{
					int counter = 0;
					for(int r = 0; r < motherMatrix[0][0].length; r++)
					{
						for(int c = 0; c < motherMatrix[0][0][0].length; c++)
						{
							boolean markIt = true;
							for(int i = 0; i < motherMatrix[0][0][0][0].length; i++)
							{
								boolean asked = false;
								for(int j = 0; j < inquiry.length; j++)
								{
									if(inquiry[j] == i)
									{
										asked = true;
									}
								}	
								if(asked)
								{
									if(motherMatrix[rr][cc][r][c][i] == false)
									{
										markIt = false;
									}
								}
								else
								{
									if(motherMatrix[rr][cc][r][c][i])
									{
										markIt = false;
									}
								}
							}
							if(markIt)
							{
								DisplayPic.drawTargets(rr, cc, r, c);
								displayMatrix[r][c] = true;
								counter++;
							}
							else 
							{
								displayMatrix[r][c] = false;
							}
						
									
						}
					}
					if(counter > 0)
					{
						table.write("Location: (" + rr + ", " + cc + ")\n");
						System.out.println("Location: (" + rr + ", " + cc + ")\n");
						for(int r = 0; r <= displayMatrix.length; r++)
						{
							if(r == 0)
							{
									table.write("   A B C D E F G H I J K L M N \n");
									System.out.println("   A B C D E F G H I J K L M N \n");
							}
							else
							{
								for(int c = 0; c <= displayMatrix[0].length; c++)
								{
								  if(c == 0 )
								  {
										if(r < 10){                       
											table.write(r + "  ");
											System.out.print(r + "  ");}
										else{
											table.write(r + " ");
											System.out.print(r + " ");}
								  }
								  else
								  {  
									  String line = "";   
									  if(displayMatrix[r - 1][c - 1] == false)
									  {  
										  line = line + "- ";
									  }
									  else
									  {
										  line = line + "X ";
										  count++;
									  }
									  if(c == displayMatrix[0].length)
									  {
										  line = line + "\n";
									  }
									  table.write(line);
									  System.out.print(line);
								  }
							  }
						  }
						}
						table.write("Final Cell Count: " + count + "\n");
						System.out.println("Final Cell Count: " + count);
						numCellFound = count;
						System.out.println();
					}
				}
			}
			DisplayPic.write(PicFileName);
			table.close();
		}
		catch(IOException i)
		{
			System.out.println("Error: " + i.getMessage());
		}
	}
}

class Channel {

	private Picture plainImageFile = null;
	private Picture contrastImageFile = null;
	private int channelNumber;
	private JPanel fileChooserPanel;
	private JTextField ChannelNameTextField;
	private JLabel ChannelLabel;
	private ArrayList<Channel> channelList;
	private Channel classReference = this;
	private JLabel numChannelsLabel;
	private String filePath;
	PictureExplorer exp;
	private JPanel imageViewPanel;
	private Color colorLimt;

	public Channel(int channel, JLabel numChannelsLabel, JPanel panel, ArrayList<Channel> cList, JPanel imagepanel) {
		channelNumber = channel;
		fileChooserPanel = panel;
		channelList = cList;
		this.numChannelsLabel = numChannelsLabel;
		imageViewPanel = imagepanel;

	}

	public int getChannelNumber() {
		return channelNumber;
	}

	public void addChoosePlainFileButtonListener(JButton b, JLabel l) {
		b.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				JFileChooser chooser = new JFileChooser();

				FileNameExtensionFilter filter = new FileNameExtensionFilter("JPG & GIF Images", "jpg", "gif");
				chooser.setFileFilter(filter);
				int returnVal = chooser.showOpenDialog(null);
				if (returnVal == JFileChooser.APPROVE_OPTION)
					l.setText(chooser.getSelectedFile().getName());
				if (channelNumber == 1) {
					System.out.println(chooser.getSelectedFile().getAbsolutePath());
					try {
						ImageInterface.drawPicture(chooser.getSelectedFile().getAbsolutePath());
					} catch (IOException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
				}
				plainImageFile = new Picture(chooser.getSelectedFile().getAbsolutePath());
				filePath = chooser.getSelectedFile().getAbsolutePath();
			}
		});
	}

	public void addChooseContrastFileButtonListener(JButton b, JLabel l) {
		b.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				JFileChooser chooser = new JFileChooser();

				FileNameExtensionFilter filter = new FileNameExtensionFilter("JPG & GIF Images", "jpg", "gif");
				chooser.setFileFilter(filter);
				int returnVal = chooser.showOpenDialog(null);
				if (returnVal == JFileChooser.APPROVE_OPTION)
					l.setText(chooser.getSelectedFile().getName());
				contrastImageFile = new Picture(chooser.getSelectedFile().getAbsolutePath());
			}
		});
	}
	public void addButtonHoverEffectGreen(JButton button)
	{
		button.addMouseListener(new java.awt.event.MouseAdapter() {
		    public void mouseEntered(java.awt.event.MouseEvent evt) {
		        button.setBackground(Color.GREEN);
		    }

		    public void mouseExited(java.awt.event.MouseEvent evt) {
		        button.setBackground(UIManager.getColor("control"));
		    }
		});
	}
	public void addButtonHoverEffectRed(JButton button)
	{
		button.addMouseListener(new java.awt.event.MouseAdapter() {
		    public void mouseEntered(java.awt.event.MouseEvent evt) {
		        button.setBackground(Color.RED);
		    }

		    public void mouseExited(java.awt.event.MouseEvent evt) {
		        button.setBackground(UIManager.getColor("control"));
		    }
		});
	}
	
	public void addChannel() {

		ChannelLabel = new JLabel("Channel #" + channelNumber + ":");
		fileChooserPanel.add(ChannelLabel);

		ChannelNameTextField = new JTextField();
		fileChooserPanel.add(ChannelNameTextField);

		JButton plainImageButton = new JButton("Choose Plain Image", new ImageIcon("chooose_image_icon.png"));
		JLabel plainImageLabel = new JLabel("No file chosen");
		addChoosePlainFileButtonListener(plainImageButton, plainImageLabel);
		addButtonHoverEffectGreen(plainImageButton);
		
		JButton removeChannelButton = new JButton("Remove Channel", new ImageIcon("cross.jpg"));
		addButtonHoverEffectRed(removeChannelButton);
		JButton contrastImageButton = new JButton("Choose Contrast Image", new ImageIcon("chooose_image_icon.png"));
		JLabel contrastImageLabel = new JLabel("No file chosen");
		addButtonHoverEffectGreen(contrastImageButton);
		addChooseContrastFileButtonListener(contrastImageButton, contrastImageLabel);
		JLabel EmptySpace = new JLabel("");
		JLabel EmptySpace1 = new JLabel("");
		removeChannelButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				numChannelsLabel.setText("Number of Channels: " + (channelList.size() - 1));

				fileChooserPanel.remove(ChannelLabel);
				fileChooserPanel.remove(ChannelNameTextField);
				fileChooserPanel.remove(plainImageButton);
				fileChooserPanel.remove(plainImageLabel);
				fileChooserPanel.remove(removeChannelButton);
				fileChooserPanel.remove(contrastImageButton);
				fileChooserPanel.remove(contrastImageLabel);
				fileChooserPanel.remove(contrastImageLabel);
				fileChooserPanel.remove(EmptySpace);
				fileChooserPanel.remove(EmptySpace1);
				ImageInterface.deleteChannel(classReference);
				ImageInterface.setUpInputOutputPanel();

				for (int i = 0; i < channelList.size(); i++) {
					channelList.get(i).changeChannelNumber(i + 1);
				}
				redrawChannel();
				if (channelNumber == 1) {
					imageViewPanel.removeAll();
					if (channelList.size() > 0 && channelList.get(0).getPlainImageFile() != null)
						exp = new PictureExplorer(channelList.get(0).getPlainImageFile(), imageViewPanel);
					imageViewPanel.revalidate();
					imageViewPanel.repaint();
				}
				//setUpInputOutputPanel();
			}
		});

		fileChooserPanel.add(removeChannelButton);
		fileChooserPanel.add(plainImageButton);
		fileChooserPanel.add(plainImageLabel);

		fileChooserPanel.add(EmptySpace);
		fileChooserPanel.add(contrastImageButton);
		fileChooserPanel.add(contrastImageLabel);

		fileChooserPanel.add(EmptySpace1);

		System.out.println("Channel #" + channelNumber + " Added");

	}

	public void clearName() {
		ChannelNameTextField.setText("");
		redrawChannel();
	}

	public Picture getContrastImageFile() {
		return contrastImageFile;
	}

	public Picture getPlainImageFile() {
		return plainImageFile;
	}

	public String getName() {
		return ChannelNameTextField.getText();
	}

	public void changeChannelNumber(int n) {
		channelNumber = n;
		ChannelLabel.setText("Channel #" + channelNumber + ":");
		redrawChannel();
	}

	public void redrawChannel() {
		fileChooserPanel.revalidate();
		fileChooserPanel.repaint();

	}

	public String getFilePath() {
		return filePath;
	}
}

class PixelRange {
	private int lowerBound = 0;
	private int upperBound = 0;

	public PixelRange(int l, int u) {
		lowerBound = l;
		upperBound = u;
	}

	public void setLower(int lower) {
		lowerBound = lower;
	}

	public void setUpper(int higher) {
		upperBound = higher;
	}

	public int getLower() {
		return lowerBound;
	}

	public int getUpper() {
		return upperBound;
	}
}
