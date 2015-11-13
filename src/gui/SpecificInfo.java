package gui;

import javafx.beans.binding.Bindings;
import javafx.beans.binding.ObjectBinding;
import javafx.beans.binding.StringBinding;
import javafx.beans.binding.StringExpression;
import javafx.beans.property.ObjectProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import javafx.scene.text.Font;
import world.Critter;
import world.Entity;
import javafx.scene.image.ImageView;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;

import javax.naming.Binding;

public class SpecificInfo extends AnchorPane {
	
	Controller controller;
	TextFlow info;
	ImageView picture = new ImageView();


	PropertyBinding critterStatus;


	public SpecificInfo(Controller c) {
		this.controller = c;
		critterStatus = new PropertyBinding();
		info = new TextFlow();
		info.setLayoutY(60);
		AnchorPane.setTopAnchor(info, 60.0);
		AnchorPane.setLeftAnchor(picture, 30.0);
		AnchorPane.setLeftAnchor(info, 30.0);
		critterStatus.addListener( new ChangeListener<Object>() {

			@Override
			public void changed(ObservableValue observable, Object oldValue, Object newValue) {
				Entity e = critterStatus.getValue();
				if (e == null){
					info.getChildren().clear();
					picture.setImage(null);
					return;
				}
				File imagefile = null;
				switch(e.getClass().toString()) {
				case "class world.Critter":
					imagefile = new File("critter.png");
					break;
				case "class world.Food":
					imagefile = new File("food2.png");
					break;
				case "class world.Rock":
					imagefile = new File("rock.png");
					break;
				default:
					imagefile = new File("critter.png");
					break;
				}
				try{
					picture.setImage(new Image(new FileInputStream(imagefile)));
					picture.setFitHeight(50);
					picture.setFitWidth(50);
					getChildren().set(0, picture);
				}
				catch(FileNotFoundException e1) {
					e1.printStackTrace();
				}
				info.getChildren().clear();
				for (String s: e.properties()) {
					Text t = new Text();
					int n = s.indexOf(":");
					t.setFont(Font.font("System", FontWeight.BOLD,14.0));
					t.setText(s.substring(0,n));
					Text t2 = new Text();
					t2.setText(s.substring(n,s.length()) + "\n");
					info.getChildren().addAll(t,t2);
				}
				
			}

			

			
		});
		this.getChildren().add(picture);
		this.getChildren().add(info);
	}


	private class PropertyBinding extends ObjectBinding<Entity>{
		public PropertyBinding(){
			super();
			bind(controller.focused);
		}
		@Override
		protected Entity computeValue() {
			System.out.println("Checkpoint 2");
			if(controller.focused.getValue() != null) {
				return controller.focused.getValue();
			}
			return null;
		}
		public void bind(ObjectProperty<Entity> e){

			super.bind(e);
		}
	}

	public void bind(ObjectProperty<Entity> e){
		critterStatus.bind(e);
	}
}