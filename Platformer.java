import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class Platformer extends JFrame implements ActionListener, KeyListener
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JTextField inputField;
	private JLabel[][] labelGrid;
	private int[] coordinates;
	private final int GRID_WIDTH = 20;
	private final int GRID_HEIGHT = 20;
			
	public static void main(String args[])
	{
		new Platformer();
	}
	
	public Platformer()
	{
		this.setSize(200, 200);
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
		this.setLayout(new GridLayout(GRID_HEIGHT+1, GRID_WIDTH));
		
		labelGrid = new JLabel[GRID_HEIGHT][GRID_WIDTH];
		
		coordinates = new int[2];
		coordinates[0] = 1;
		coordinates[1] = 1;
		
		Timer timer = new Timer(500, this);
		timer.start(); 
		
		for(int c = 0; c<GRID_HEIGHT; c++)
		{
			for(int d = 0; d<GRID_WIDTH; d++)
			{
				labelGrid[c][d] = new JLabel(" ");
				this.add(labelGrid[c][d]);
			}
		}
		
		inputField = new JTextField();
		inputField.addKeyListener(this); 
		this.add(inputField);
		
		resetGrid();
		
		this.setVisible(true);
	}
	
	public void actionPerformed(ActionEvent e)
	{
		if(coordinates[0]<GRID_HEIGHT-1)
			coordinates[0]++;
		
		resetGrid();
	}
	
	public void resetGrid()
	{
		for(int c = 0; c <GRID_HEIGHT; c++)
		{
			for(int d = 0; d <GRID_WIDTH; d++)
				labelGrid[c][d].setText("");
		}
		
		labelGrid[coordinates[0]][coordinates[1]].setText("X");
		
		inputField.setText("");
	}

	@Override
	public void keyPressed(KeyEvent arg0){
		String key = KeyEvent.getKeyText(arg0.getKeyCode()).toLowerCase();
		//System.out.print(key);
		switch(key){
		case "w":
			inputField.setText("");
			
			for(int c = 0; c<3; c++)
			{
				if(isMoveable(coordinates[0]-1, coordinates[1]))
					coordinates[0]--;
				
			}
			
			resetGrid();
			break;
		case "s":
			inputField.setText("");
			
			if(isMoveable(coordinates[0]+1, coordinates[1]))
				coordinates[0]++;
			
			resetGrid();
			break;
		case "a":
			inputField.setText("");
			
			if(isMoveable(coordinates[0], coordinates[1]-1))
				coordinates[1]--;
			
			resetGrid();
			break;
		case "d":
			inputField.setText("");
			
			if(isMoveable(coordinates[0], coordinates[1]+1))
				coordinates[1]++;
			
			resetGrid();
			break;
		default:
			resetGrid();
			break;
		}
	}

	@Override
	public void keyReleased(KeyEvent arg0) {
		
		
	}

	@Override
	public void keyTyped(KeyEvent arg0) {
		
		
	}
	
	public boolean isMoveable(int row, int col)
	{
		return row >= 0 && row < GRID_HEIGHT && col >= 0 && col < GRID_WIDTH ;
	}
}
