package org.alexdev.kepler.game.pathfinder.game.bresenhamsLine;

import org.alexdev.kepler.game.pathfinder.Position;

import java.awt.Point;
import java.util.ArrayList;

public class BresenhamsLine {
	public static ArrayList<Position> getPointsOnLine(Position p1, Position p2) {

		Position a = (Position) p1.copy();
		Position b = (Position) p2.copy();
		
		ArrayList<Position> pointsOnLine = new ArrayList<>();
		
		boolean steep = Math.abs(b.getY() - a.getY()) > Math.abs(b.getX() - a.getX());
		
		if(steep) {
			// swap(a.getX(), a.getY())
			int temp;
			temp = a.getX();
			a.setX(a.getY());
			a.setY(temp);
			// swap(b.getX(), b.getY())
			temp = a.getX();
			b.setX(b.getY());
			b.setY(temp);
		}
		if(a.getX() > b.getX()) {
			// swap(a.getX(), b.getX())
			int temp;
			temp = a.getX();
			a.setX(b.getX());
			b.setX(temp);
			// swap(a.getY(), b.getY())
			temp = a.getY();
			a.setY(b.getY());
			b.setY(temp);
		}
		
		int deltaX = b.getX() - a.getX();
		int deltaY = Math.abs(b.getY() - a.getY());
		int error = deltaX/2;
		
		int yStep;
		int y = a.getY();
		if (a.getY() < b.getY()) 
			yStep = 1;
		else
			yStep = -1;
		
		for(int x=a.getX(); x<=b.getX(); x++) {
			if(steep)
				pointsOnLine.add(new Position(y,x));
			else
				pointsOnLine.add(new Position(x,y));
			error = error - deltaY;
			if(error<0) {
				y = y + yStep;
				error = error + deltaX;
			}
		}
					
		return pointsOnLine;
		
//		function line(x0, x1, y0, y1)
//	     boolean steep := abs(y1 - y0) > abs(x1 - a.getX())
//	     if steep then
//	         swap(a.getX(), y0)
//	         swap(x1, y1)
//	     if a.getX() > x1 then
//	         swap(a.getX(), x1)
//	         swap(y0, y1)
//	     int deltax := x1 - a.getX()
//	     int deltay := abs(y1 - y0)
//	     int error := deltax / 2
//	     int ystep
//	     int y := y0
//	     if y0 < y1 then ystep := 1 else ystep := -1
//	     for x from a.getX() to b.getX()
//	         if steep then plot(y,x) else plot(x,y)
//	         error := error - deltay
//	         if error < 0 then
//	             y := y + ystep
//	             error := error + deltax

	}
	
}