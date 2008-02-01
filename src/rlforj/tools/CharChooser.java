package rlforj.tools;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.Window;
import java.awt.Dialog.ModalityType;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ToolTipManager;
import javax.swing.WindowConstants;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;

public class CharChooser extends JComponent implements MouseListener
{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	protected static final int NUM_COLS = 64;
	private char[][] dispC;
	private int fontw;
	private int fonth;
	private int fontbase;

	int selX=-1, selY=-1;
	private char selChar=5;
	private Character returnChar=selChar;
	
	public CharChooser() {
		setFont(new Font("Courier New", Font.PLAIN, 10));
//		System.out.println("SetFont "+getFont());
		addMouseListener(this);
		
		ToolTipManager.sharedInstance().registerComponent(this);
	}
	
	public static void main(String[] args)
	{
		
		CharChooser c=new CharChooser();
		c.displayDialog(null);
		System.out.println("Done");
		System.out.println((int)c.getSelectedChar());
		System.exit(0);

	}
	
	public void displayDialog(Window owner) {
		final JDialog jf=new JDialog(owner, "Choose Character", ModalityType.APPLICATION_MODAL);
		jf.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
		
		final Container contentPane = jf.getContentPane();
		contentPane.setLayout(new BorderLayout());
		JButton button = new JButton("Choose font");
		final CharChooser charChooser = this;
		
		
		final FontChooser fc=new FontChooser(jf);
	    SimpleAttributeSet a = new SimpleAttributeSet();
	    StyleConstants.setFontFamily(a, "Courier New");
	    StyleConstants.setFontSize(a, 10);
	    fc.setAttributes(a);
	    
		JPanel north=new JPanel();
		north.setLayout(new BoxLayout(north, BoxLayout.X_AXIS));
		
		final JLabel fontLabel=new JLabel("Displayable characters in Font: "+charChooser.getFont().getFamily());
		north.add(fontLabel);
		north.add(Box.createHorizontalStrut(20));
		north.add(button);
		contentPane.add(north, BorderLayout.NORTH);
		
		button.addActionListener(new ActionListener() {
			
			public void actionPerformed(ActionEvent e)
			{
				fc.setVisible(true);
//				System.out.println("done");
				if(fc.getOption()!=JOptionPane.CANCEL_OPTION){
					Font selectedFont = fc.getSelectedFont();
					System.out.println(selectedFont.getAttributes());
					charChooser.setFont(selectedFont);
					
					fontLabel.setText("Displayable characters in Font: "+charChooser.getFont().getFamily());
					
					jf.pack();
					charChooser.repaint();
				}
			}
			
		});
		
		contentPane.add(new JScrollPane(charChooser));
		
		JPanel bottom=new JPanel();
		JButton ok=new JButton("Ok");
		ok.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e)
			{
				returnChar=selChar;
				jf.setVisible(false);
				jf.dispose();
			}
		});
		JButton cancel=new JButton("Cancel");
		cancel.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e)
			{
				returnChar=null;
				jf.setVisible(false);
				jf.dispose();
			}
		});
		contentPane.add(bottom, BorderLayout.SOUTH);
		
		bottom.add(ok);
		bottom.add(cancel);
		jf.pack();
		jf.setVisible(true);
		
	}
	
	@Override
	public void setFont(Font f) {
		f=f.deriveFont((float)f.getSize2D()+5);
		super.setFont(f);
		
		FontMetrics fm=getFontMetrics(f);
		fontw = fm.getMaxAdvance();
		fonth = fm.getMaxAscent()+fm.getMaxDescent();
		fontbase = fm.getMaxAscent();
		
		int numGlyphs = 0;//f.getNumGlyphs()*2;
		for(int i=0; i<Character.MAX_CODE_POINT; i++)
			if(f.canDisplay(i))
				numGlyphs++;
		dispC = new char[numGlyphs/NUM_COLS+1][NUM_COLS];
//		System.out.println("ng "+numGlyphs);
		int c=0;
		for(int i=0; i<Character.MAX_CODE_POINT; i++)
		{
			if(f.canDisplay(i)){
				dispC[c/NUM_COLS][c%NUM_COLS]=(char)i;
				c++;
				if(c>numGlyphs)
					break;
			}
		}
		for(int i=c; i<(numGlyphs/NUM_COLS+1)*NUM_COLS; i++) {
			dispC[i/NUM_COLS][i%NUM_COLS]=0;
		}
		
//		System.out.println(" "+dispC.length+" "+fontw+" "+fonth);
		setSize(fontw*NUM_COLS+30, fonth*dispC.length+10);
		setPreferredSize(getSize());
		invalidate();
	}
	
	@Override
	public String getToolTipText(MouseEvent e){
		int x=e.getX(); int y=e.getY();
		
		x/=fontw; y/=fonth;
		
		if(dispC==null)
			return null;
		if(y>=dispC.length || x>=dispC[0].length)
			return null;
		return Integer.toString(dispC[y][x])+"/0x"+Integer.toHexString(dispC[y][x]);
	}
	
	@Override
	public void paintComponent(Graphics g){
		Graphics2D g2=(Graphics2D) g.create();
		
		g2.setColor(Color.white);
		g2.fill(g.getClip());
		
		if(dispC==null)
			return;
		
		g2.setColor(getForeground());
		
		Rectangle r=new Rectangle(0, 0, fontw, fonth);
		Rectangle clipBounds = g.getClipBounds();
		for(int i=0; i<dispC.length; i++){
			for(int j=0; j<dispC[0].length; j++){
				r.x=fontw*j;
				r.y=i*fonth;
				if(r.intersects(clipBounds)) {
					if(selX==j && selY==i)
						g2.draw(r);
					g2.drawString(Character.toString(dispC[i][j]), fontw*j, i*fonth+fontbase);
				}
				
			}
		}
	}
	public void mouseClicked(MouseEvent e)
	{
		selX=e.getX(); selY=e.getY();
		selX/=fontw; selY/=fonth;
		if(dispC!=null && selY<dispC.length && selX<dispC[0].length)
			selChar=dispC[selY][selX];
		System.out.println(selX+" "+selY+" "+(int)selChar);
		repaint();
	}
	public void mouseEntered(MouseEvent e)
	{
	}
	public void mouseExited(MouseEvent e)
	{
	}
	public void mousePressed(MouseEvent e)
	{
	}
	public void mouseReleased(MouseEvent e)
	{
	}
	public Character getSelectedChar()
	{
		return returnChar;
	}
	
	public Font getSelectedFont()
	{
		Font f=super.getFont();
		if(f==null)
			return null;
		return f.deriveFont(f.getSize2D()-5.0F);
	}
}
