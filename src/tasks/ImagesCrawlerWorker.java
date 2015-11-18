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

public class ImagesCrawlerWorker extends SwingWorker<ArrayList<String>, String> {

	private String url;
	private String filter;
	private FileTableModel model;
	private InputComponent inputComp;
	
	public ImagesCrawlerWorker(String url, String filter, FileTableModel model, InputComponent inputComp) { // FileListModel model, InputComponent inputComp
		this.url = url;
		this.filter = filter;
		this.model = model;
		this.inputComp = inputComp;
	}
	
	@Override
	protected ArrayList<String> doInBackground() throws Exception {
		Document doc = Jsoup.connect(url).get();
		ArrayList<String> images = new ArrayList<String>();
		
		Elements elements = doc.getElementsByTag("img"); // ~=(?i)\\.(png|jpe?g)
		for (Element element : elements) {
			String link = element.attr("abs:src");
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
