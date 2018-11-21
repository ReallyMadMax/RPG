package GameData;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;

public class Serializer implements Serializable
{
	private static final long serialVersionUID = 1L;

	static public void out(String string, ArrayList<Double> upgrades){
		String fileName = string + ".txt";
		
		try {
			FileOutputStream fos = new FileOutputStream(fileName);
			ObjectOutputStream oos = new ObjectOutputStream(fos);
			oos.writeObject(upgrades);
			oos.close();
		} catch (IOException e){
			System.out.println("IOException");
		}
	}
		
	
	static public ArrayList<Double> in(String string) {
		String fileName = string + ".txt";
		try {
		FileInputStream fis = new FileInputStream(fileName);
		ObjectInputStream ois = new ObjectInputStream(fis);
		ArrayList<Double> object = (ArrayList<Double>) ois.readObject();
		ois.close();
		return object;
		} catch ( FileNotFoundException e ){
			System.out.println("FileNotFoundException");
			return null;
		} catch (IOException e){
			System.out.println("IOException");
			return null;
		} catch (ClassNotFoundException e){
			System.out.println("ClassNotFoundException");
			return null;
		}
	}
	
	
	static public void ToFile(String string, double money) {

		String file = string + ".txt";
		try{
		    FileWriter writer = new FileWriter(file);
		    BufferedWriter out = new BufferedWriter(writer);
		    //out.newLine();
		    
		    out.write(Double.toString(money));
		  
		    
		    out.close();
		} catch (IOException e) {
			
		}
	}
	
	static public double NFromFile(String string){
		String output = "";
		String file = string + ".txt";
		
		
		try (BufferedReader br = new BufferedReader(new FileReader(file))) {

			String sCurrentLine;

			while ((sCurrentLine = br.readLine()) != null) {
				output = sCurrentLine;
			}

		} catch (IOException e) {
			
		}
		
		double returnValue = Double.parseDouble(output);
		
		return (returnValue);
	}

}
