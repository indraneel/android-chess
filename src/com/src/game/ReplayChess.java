package com.src.game;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Scanner;

/**
 * Testing playback.
 * @author kyle
 */
public class ReplayChess {
	private static Scanner scanner;
	
	public static void main(String[] args) {
		scanner = new Scanner(System.in);
		
		System.out.println("Enter chess file:");
		String filename = scanner.nextLine();
		String playbackString = null;
		try {
			BufferedReader br = new BufferedReader(new FileReader(filename));
			playbackString = br.readLine().trim();
		}
		catch(IOException e) {
			System.out.println("An error occurred while loading.");
			System.exit(1);
		}
		
		Playback p = Playback.fromString(playbackString);
		if(p == null) {
			System.out.println("A fatal error occurred.");
			System.exit(1);
		}
		p.startPlayback();
		System.out.println(p.getGame().getGrid().draw() + "\n");
		while(true) {
			String input = scanner.nextLine();
			if(input.equalsIgnoreCase("next")) {
				if(p.hasNext())
					p.next();
				else
					System.out.println("No more moves forward in the playback.");
			}
			else if(input.equalsIgnoreCase("previous") || input.equalsIgnoreCase("prev")) {
				if(p.hasPrevious())
					p.previous();
				else
					System.out.println("No more moves backwards in the playback.");
			}
			else {
				System.out.println("Invalid input.");
			}
			
			System.out.println();
			System.out.println(p.getGame().getGrid().draw() + "\n");
		}
	}
}