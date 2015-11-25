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

/**
 * Represents a file crawler
 */
public class FileCrawlerWorker extends SwingWorker<ArrayList<String>, String> {

	private String url;
	private String extensionFilter;
	private FileTableModel model;
	private InputComponent inputComp;
	
	/**
	 * Creates a new file crawler worker
	 * @param url The site's URL
	 * @param extensionFilter The extension filter
	 * @param model The table's model
	 * @param inputComp The input section
	 */
	public FileCrawlerWorker(String url, String extensionFilter, FileTableModel model, InputComponent inputComp) {
		this.url = url;
		this.extensionFilter = extensionFilter;
		this.model = model;
		this.inputComp = inputComp;
	}
	
	@Override
	protected ArrayList<String> doInBackground() throws Exception {
		// Connect to the site
		Document doc = Jsoup.connect(url).get();
		ArrayList<String> links = new ArrayList<String>();
		
		// Get the elements according to the filter
		Elements elements = extensionFilter.isEmpty()
				? doc.select("img, a")
				: doc.select("img[src~=(?i)\\.(" + extensionFilter + ")|(" + extensionFilter + ")], a[href~=(?i)\\.(" + extensionFilter + ")|(" + extensionFilter + ")]");
		
		// Get links from matched elements
		for (Element element : elements) {
			String link = element.tagName() == "img" ? element.attr("abs:src") : element.attr("abs:href");
			
			if (!links.contains(link)) {
				links.add(link);
				publish(link);
			}
		}
		
		return links;
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
