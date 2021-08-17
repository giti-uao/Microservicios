package api.endpoints.springbootactuator.detection;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;

import org.datavec.image.loader.NativeImageLoader;
import org.deeplearning4j.nn.graph.ComputationGraph;
import org.deeplearning4j.nn.layers.objdetect.DetectedObject;
import org.deeplearning4j.nn.layers.objdetect.Yolo2OutputLayer;
import org.deeplearning4j.zoo.model.TinyYOLO;
import org.nd4j.linalg.api.ndarray.INDArray;
import org.nd4j.linalg.dataset.api.preprocessor.ImagePreProcessingScaler;

import api.endpoints.springbootactuator.models.*;

public class yolo {
	private static String[] labels = {"aeroplane","bicycle","bird","boat","bottle","bus","car","cat","chair","cow",
			"diningtable","dog","horse","motorbike","person","pottedplant","sheep","sofa","train","tvmonitor"};
	

	public List<objProperties> detect_objs(double dt, String img_url) {
		List<objProperties> salida = new ArrayList<objProperties>();
		try {
			ComputationGraph model;
			model = (ComputationGraph) TinyYOLO.builder().build().initPretrained();
			NativeImageLoader loader = new NativeImageLoader(416, 416, 3);
			ImagePreProcessingScaler imagePreProcessingScaler = new ImagePreProcessingScaler(0, 1);
			Yolo2OutputLayer outputLayer = (Yolo2OutputLayer) model.getOutputLayer(0);
			URL imageURL = new URL(img_url);
			BufferedImage img=ImageIO.read(imageURL);
			INDArray indArray = loader.asMatrix(img);
			imagePreProcessingScaler.transform(indArray);
			INDArray results = model.outputSingle(indArray);
			List<DetectedObject> detectedObjects = outputLayer.getPredictedObjects(results, dt);
			salida = detectedObjectstojson(img_url, detectedObjects); //Drawing detected objects
			return salida;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			System.out.print("Error al detectar objetos");
			e.printStackTrace();
		}
		return salida;
	}

	private static List<objProperties> detectedObjectstojson(String img_url, List<DetectedObject> detectedObjects) throws IOException {
		
		List<objProperties> objProperties_list = new ArrayList<objProperties>();
		URL imageURL = new URL(img_url);
		BufferedImage img=ImageIO.read(imageURL);
		Graphics2D g2d = img.createGraphics();
		g2d.setColor(Color.RED);
		g2d.setStroke(new BasicStroke(2));
		for (DetectedObject detectedObject : detectedObjects) {
			double confidence = detectedObject.getConfidence();
			double x1 = detectedObject.getTopLeftXY()[0];
			double y1 = detectedObject.getTopLeftXY()[1];
			double x2 = detectedObject.getBottomRightXY()[0];
			double y2 = detectedObject.getBottomRightXY()[1];
			int xs1 = (int) ((x1 / 13.0 ) * (double) img.getWidth());
			int ys1 = (int) ((y1 / 13.0 ) * (double) img.getHeight());
			int xs2 = (int) ((x2 / 13.0 ) * (double) img.getWidth());
			int ys2 = (int) ((y2 / 13.0 ) * (double) img.getHeight());
			objProperties obj_properties = new objProperties(labels[detectedObject.getPredictedClass()],xs1,ys1,xs2-xs1,ys2-ys1,confidence);
			objProperties_list.add(obj_properties);
		}
		return objProperties_list;
	}


}
