package rlforj.ui.ascii;

import java.awt.Color;
import java.awt.Font;

import rlforj.ui.Visual;

/**
 * A displayable character to be displayed as a tile, 
 * along with color and font in case it needs special 
 * displays.
 * Unicode characters are allowed.
 * @author sdatta
 *
 */
public class CharVisual extends Visual
{
	public static CharVisual BLANK=new CharVisual(' ', Color.black),
							 TRANSPARENT=null;
	
	/**
	 * the character to display
	 */
	public char disp=' ';
	/**
	 * The color which to use to display.
	 */
	public Color col=Color.WHITE;
	
	/**
	 * The special font to use to display.
	 * Null=> default
	 */
	public Font font=null;

	/**
	 * Create a character of a specified color.
	 * @param disp
	 */
	public CharVisual(char disp, Color col)
	{
		this.disp = disp;
		this.col=col;
	}
	
	/**
	 * A darker charvisual for display when invisible
	 * @return
	 */
	public CharVisual darker() {
		return new CharVisual(disp, col.darker());
	}

	@Override
	public int hashCode()
	{
		final int PRIME = 31;
		int result = 1;
		result = PRIME * result + ((col == null) ? 0 : col.hashCode());
		result = PRIME * result + disp;
		result = PRIME * result + ((font == null) ? 0 : font.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj)
	{
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		final CharVisual other = (CharVisual) obj;
		if (col == null)
		{
			if (other.col != null)
				return false;
		} else if (!col.equals(other.col))
			return false;
		if (disp != other.disp)
			return false;
		if (font == null)
		{
			if (other.font != null)
				return false;
		} else if (!font.equals(other.font))
			return false;
		return true;
	}
	
}
