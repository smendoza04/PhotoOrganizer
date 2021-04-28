/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package photoorganizer;

import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

/**
 *
 * @author User
 */
public class IconGraphic extends Button{
    
    public IconGraphic(Image iv) {
        ImageView image = new ImageView(iv);
        image.setFitHeight(17);
        image.setFitHeight(17);
        image.setPreserveRatio(true);
        setGraphic(image);
    }
}