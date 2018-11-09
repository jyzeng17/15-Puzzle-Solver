package io.github.jyzeng17;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JButton;
import javax.swing.border.MatteBorder;
import javax.swing.SwingConstants;
import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.FlowLayout;
import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

public class GUI implements ActionListener {
	private static final int MARGIN_WIDTH = 30;
	private static final int CELL_SIZE = 100;
	private static final int INFORMATION_LABEL_HEIGHT = 40;
	private static final int BUTTONS_PANEL_HEIGHT = 50;

	private Puzzle puzzle;
	private JFrame frame;
	private JLabel informationLabel;
	private JLabel[] cellLabels;
	private JButton button;

	private ArrayList<Ply> solutionPlys;
	private int zeroPosition;
	private int numberOfTotalMoves;

	public GUI() {
		puzzle = new Puzzle();
		solutionPlys = new ArrayList<Ply>();
		zeroPosition = -1;

		initializeFrame();

		initializeInformationLabel();

		initializePuzzlePanel();

		initializeButtonsPanel();
	}

	public void setVisible(boolean b) {
		frame.setVisible(b);
	}

	public void actionPerformed(ActionEvent e) {
		if (e.getActionCommand().equals("Initialize")) {
			puzzle.initialize();
			updateStartingState();

			informationLabel.setText("Ready to start.");

			button.setText("Start Searching");
		}
		else if (e.getActionCommand().equals("Start Searching")) {
			informationLabel.setText("Running searching algorithm...");

			button.setVisible(false);

			// Need to run in another thread so that GUI can be refreshed using this thread
			new Thread() {
				public void run() {
					puzzle.startTiming();

					Node[] solutionNodes = puzzle.bIDAStar();

					long elapsedTime = puzzle.getElapsedTime();
					informationLabel.setText("Finished. Elapsed time: " + elapsedTime + " ms");

					solutionPlys.addAll(solutionNodes[0].getPath().getPath());
					solutionPlys.addAll(solutionNodes[1].getPath().getReversePath());

					io.github.jyzeng17.State startingState = puzzle.getStartingState();
					for (int i = 0; i < Puzzle.SIZE; ++i) {
						if (startingState.get(i) == 0) {
							zeroPosition = i;
							break;
						}
					}

					button.setText("Check Solution");
					button.setVisible(true);
					System.out.println("\nFinished!");
				}
			}.start();
		}
		else if (e.getActionCommand().equals("Check Solution")) {
			numberOfTotalMoves = solutionPlys.size();
			informationLabel.setText("Number of moves left: " + numberOfTotalMoves + " / " + numberOfTotalMoves);
			button.setText("Next Move");
		}
		else if (e.getActionCommand().equals("Next Move")) {
			Ply ply = solutionPlys.remove(0);

			// Switch 0's cell and corresponding cell at GUI level
			switch (ply) {
				case UP:
					cellLabels[zeroPosition].setText(cellLabels[zeroPosition - Puzzle.EDGE_SIZE].getText());
					zeroPosition = zeroPosition - Puzzle.EDGE_SIZE;
					break;
				case DOWN:
					cellLabels[zeroPosition].setText(cellLabels[zeroPosition + Puzzle.EDGE_SIZE].getText());
					zeroPosition = zeroPosition + Puzzle.EDGE_SIZE;
					break;
				case LEFT:
					cellLabels[zeroPosition].setText(cellLabels[zeroPosition - 1].getText());
					--zeroPosition;
					break;
				case RIGHT:
					cellLabels[zeroPosition].setText(cellLabels[zeroPosition + 1].getText());
					++zeroPosition;
					break;
				default:
					break;
			}
			cellLabels[zeroPosition].setText("");

			if (solutionPlys.size() == 0) {
				button.setVisible(false);
			}
			informationLabel.setText("Number of moves left: " + solutionPlys.size() + " / " + numberOfTotalMoves);
		}
	}

	private void initializeFrame() {
		frame = new JFrame("15 puzzle");

		int width = MARGIN_WIDTH * 2 + Puzzle.EDGE_SIZE * CELL_SIZE;
		int height = INFORMATION_LABEL_HEIGHT + Puzzle.EDGE_SIZE * CELL_SIZE + BUTTONS_PANEL_HEIGHT;
		frame.setSize(width, height);

		frame.setLayout(new BorderLayout());

		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}

	private void initializeInformationLabel() {
		informationLabel = new JLabel("Initialize the puzzle first.");

		informationLabel.setHorizontalAlignment(SwingConstants.CENTER);

		informationLabel.setFont(new Font(informationLabel.getFont().getName(), Font.PLAIN, 18));

		frame.add(informationLabel, BorderLayout.NORTH);
	}

	private void initializePuzzlePanel() {
		JPanel puzzlePanel = new JPanel();

		puzzlePanel.setLayout(new GridLayout(Puzzle.EDGE_SIZE, Puzzle.EDGE_SIZE));

		cellLabels = new JLabel[Puzzle.SIZE];
		for (int i = 0; i < cellLabels.length; ++i) {
			cellLabels[i] = new JLabel("");
			int top = 1;
			int left = 1;
			int bottom = (i < Puzzle.SIZE - Puzzle.EDGE_SIZE)? 0 : 1;
			int right = ((i + 1) % Puzzle.EDGE_SIZE != 0)? 0 : 1;
			cellLabels[i].setBorder(new MatteBorder(top, left, bottom, right, Color.BLACK));
			cellLabels[i].setHorizontalAlignment(SwingConstants.CENTER);

			cellLabels[i].setFont(new Font(cellLabels[i].getFont().getName(), Font.PLAIN, 30));

			puzzlePanel.add(cellLabels[i]);
		}

		frame.add(puzzlePanel, BorderLayout.CENTER);
	}

	private void initializeButtonsPanel() {
		JPanel buttonsPanel = new JPanel();

		buttonsPanel.setLayout(new GridBagLayout());

		GridBagConstraints gbc = new GridBagConstraints();

		button = new JButton("Initialize");

		button.setFont(new Font(button.getFont().getName(), Font.PLAIN, 18));

		button.addActionListener(this);

		buttonsPanel.add(button, gbc);
		
		frame.add(buttonsPanel, BorderLayout.SOUTH);
	}

	private void updateStartingState() {
		State startingState = puzzle.getStartingState();
		
		for (int i = 0; i < Puzzle.SIZE; ++i) {
			if (startingState.get(i) == 0) {
				cellLabels[i].setText("");
				continue;
			}
			cellLabels[i].setText(Byte.toString(startingState.get(i)));
		}
	}
}
