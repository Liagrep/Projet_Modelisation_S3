package listener_package;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.JFileChooser;
import javax.swing.JOptionPane;

import sqlite.MyFileManager;
import affichages.Librairie;

public class MyButtonSaveListener implements ActionListener {

	Librairie lib;
	public MyButtonSaveListener(Librairie l) {
		this.lib = l;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		File selection = null ;
		MyFileManager f = new MyFileManager();

		JFileChooser chooser = new JFileChooser("./gts_files");
		chooser.setApproveButtonText("Selectionner");
		chooser.setDialogTitle("Selectionner");
		chooser.setApproveButtonToolTipText("Choisissez l'�l�ment � sauvegarder");
		int returnVal = chooser.showOpenDialog(null);
		if (returnVal != JFileChooser.CANCEL_OPTION) {	
			String s = chooser.getSelectedFile().getName();
			selection = chooser.getSelectedFile();	
			chooser.setApproveButtonText("Importer");
			chooser.setApproveButtonToolTipText("Sauvegarde l'�l�ment s�lectionn� dans la base de donn�es");
			chooser.setSelectedFile(selection);
			chooser.setDialogTitle("Sauvegarder");
			chooser.setCurrentDirectory(null);
			returnVal = chooser.showSaveDialog(null);
			if (returnVal != JFileChooser.CANCEL_OPTION) {	
				if(f.copier(selection.getAbsolutePath(), chooser.getSelectedFile().toString()))
					JOptionPane.showMessageDialog(null,"Le fichier "+s+" a bien �t� copi� ","Information",JOptionPane.INFORMATION_MESSAGE);
				else 
					JOptionPane.showMessageDialog(null,"Le fichier n'a pas pu �tre copi�","Erreur",JOptionPane.ERROR_MESSAGE);
			}
		}
	}

}
