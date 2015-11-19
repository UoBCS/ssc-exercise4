package tasks;

import gui.FileTableModel;
import gui.InputComponent;

import java.util.ArrayList;
import java.util.List;

import javax.swing.SwingWorker;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import utils.Utils;

public class ImagesCrawlerWorker extends SwingWorker<ArrayList<String>, String> {

	private String url;
	private String extensionFilter;
	private FileTableModel model;
	private InputComponent inputComp;
	
	public ImagesCrawlerWorker(String url, String extensionFilter, FileTableModel model, InputComponent inputComp) {
		this.url = url;
		this.extensionFilter = extensionFilter;
		this.model = model;
		this.inputComp = inputComp;
	}
	
	@Override
	protected ArrayList<String> doInBackground() throws Exception {
		Document doc = Jsoup.connect(url).get();
		ArrayList<String> images = new ArrayList<String>();
		
		Elements elements = extensionFilter.isEmpty()
				? doc.getElementsByTag("img")
				: doc.select("img[src~=(?i)\\.(" + extensionFilter + ")], a[href~=(?i)\\.(" + extensionFilter + ")]");
		
		for (Element element : elements) {
			String link = element.tagName() == "img" ? element.attr("abs:src") : element.attr("abs:href");
			
			if (!images.contains(link)) {
				images.add(link);
				publish(link);
			}
		}
		
		return images;
	}
	
	@Override
	protected void process(List<String> chunks) {
		for (String chunk : chunks) {
			model.addFile(chunk);
		}
	}
	
	protected void done() {
		inputComp.downloadBtn.setEnabled(true);
	}
	
}
