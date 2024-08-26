package com.systemwerx.common.license;

import com.systemwerx.common.event.Log;
import com.systemwerx.common.util.ExceptionUtil;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import java.io.*;
import java.lang.reflect.Method;
import java.net.URISyntaxException;
import java.util.List;

/**
 * 
 */
public class LicenseManagerUI extends Application {
	static LicenseManagerUI thelicenseManager;
	String productID = "01";
	String licensePath = "license.dat";
	String productName = "";

	// This is static to get around the crappy string only parms to a JavaFX appS
    LicenseManagerNotification notify;
	static Stage callerStage;
	Stage stage;
	boolean traceActive = false;
	boolean terminateOnLicenseApply = false;
	boolean licenseValid = false;

	public void initApp(String prodID, String iProductName, String iLicensePath, LicenseManagerNotification callback,
			boolean terminateOnApply) {
		notify = callback;
		terminateOnLicenseApply = terminateOnApply;
		productID = prodID;
		if (iLicensePath != null)
			licensePath = iLicensePath;
		if (iProductName != null)
			productName = iProductName;
		if (System.getProperty("sxdebug") != null) {
			traceActive = true;
		}

		String[] parms = new String[4];
		parms[0] = prodID;
		parms[1] = iProductName;
		parms[2] = iLicensePath;
		parms[3] = "" + terminateOnLicenseApply;
		try {
			Application.launch(parms);
		} catch (IllegalStateException e) {
			Platform.runLater(new Runnable() {
				@Override
				public void run() {
					initInCurrentApplication(prodID, iProductName, iLicensePath, null, terminateOnLicenseApply);
				}
			});
		}
	}

	public void initInCurrentApplication(String prodID, String iProductName, String iLicensePath, LicenseManagerNotification callback,
			boolean terminateOnApply) {
		try {
			Log.debug("Launch initInCurrentApplication in JaxaFX thread");
			notify = callback;
			this.stage = stage;
			terminateOnLicenseApply = terminateOnApply;
			productID = prodID;
			if (iLicensePath != null)
				licensePath = iLicensePath;
			if (iProductName != null)
				productName = iProductName;
			if (System.getProperty("sxdebug") != null) {
				traceActive = true;
			}

			launchInCurrentApplication();
		} catch (Exception ex) {
			Log.error(ex.getMessage());
		}
	}

	@Override
	public void start(Stage stage) throws IOException, URISyntaxException {
		try {
			this.stage = stage;
			// Create the FXMLLoader
			FXMLLoader loader = new FXMLLoader();
			// Path to the FXML File
			Log.info("Starting License Manager UI");
			InputStream fxmlStream = this.getClass().getResourceAsStream("/fxml/LicenseManagerUI.fxml");

			// Create the Pane and all Details
			AnchorPane root = (AnchorPane) loader.load(fxmlStream);
			// Create the Scene
			Scene scene = new Scene(root);
			// Set the Scene to the Stage
			stage.setScene(scene);
			// Set the Title to the Stage
			stage.setTitle("License Manager");
			List<String> parms = getParameters().getRaw();
			LicenseManagerUIController controller = loader.getController();
			if (parms.get(3).equals("true"))
				terminateOnLicenseApply = true;
			else
				terminateOnLicenseApply = false;
			controller.init(this, parms.get(0), parms.get(1), parms.get(2), notify, terminateOnLicenseApply);
			// Display the Stage
			stage.show();
		} catch (Exception ex) {
			Log.error("Exception " + ex.getClass().getName() + " " + ex.getMessage());
			ExceptionUtil.printStackTrace(ex);
		}
	}

	public void launchInCurrentApplication() throws IOException, URISyntaxException {
		try {
			Log.debug("Launch in current application");
			FXMLLoader loader = new FXMLLoader();
			// Path to the FXML File
			Log.info("Starting License Manager UI");
			InputStream fxmlStream = this.getClass().getResourceAsStream("/fxml/LicenseManagerUI.fxml");

			// Create the Pane and all Details
			AnchorPane root = (AnchorPane) loader.load(fxmlStream);
			// New stage
			stage = new Stage();
			// Create the Scene
			Scene scene = new Scene(root);
			// Set the Scene to the Stage
			stage.setScene(scene);
			// Set the Title to the Stage
			stage.setTitle("License Manager");
			LicenseManagerUIController controller = loader.getController();
			controller.init(this, productID, productName, licensePath, notify, terminateOnLicenseApply);
			// Display the Stage
			stage.show();
		} catch (Exception ex) {
			Log.error("Exception " + ex.getClass().getName() + " " + ex.getMessage());
			ExceptionUtil.printStackTrace(ex);
		}
	}

	void displayInfoMessage(String message) {
		Log.info(message);
	}

	void displayErrorMessage(String message) {
		Log.error(message);
	}

	public boolean isTerminateOnLicenseApply() {
		return terminateOnLicenseApply;
	}

	public void setTerminateOnLicenseApply(boolean terminateOnLicenseApply) {
		this.terminateOnLicenseApply = terminateOnLicenseApply;
	}

	public String getProductID() {
		return productID;
	}

	public void setProductID(String productID) {
		this.productID = productID;
	}

	public String getLicensePath() {
		return this.licensePath;
	}

	public void setLicensePath(String licensePath) {
		this.licensePath = licensePath;
	}

	public String getProductName() {
		return this.productName;
	}

	public void setProductName(String productName) {
		this.productName = productName;
	}

	public LicenseManagerNotification getNotify() {
		return this.notify;
	}

	public void setNotify(LicenseManagerNotification caller) {
		this.notify = caller;
	}

	public Stage getStage() {
		return this.stage;
	}

	public void setStage(Stage stage) {
		this.stage = stage;
	}
}
