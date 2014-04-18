

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.security.AllPermission;
import java.util.ArrayList;
import java.util.Date;

import perspectives.base.Property;
import perspectives.base.Task;
import perspectives.graph.GraphData;
import perspectives.graph.GraphViewer;
import perspectives.properties.PFileInput;
import perspectives.properties.PFileOutput;
import perspectives.properties.POptions;
import perspectives.properties.PSignal;
import perspectives.properties.PString;
import perspectives.properties.PText;

public class GraphUserStudyViewer extends GraphViewer {

	// tasks: pairs of graph nodes
	private String[][] tasks = new String[][] { { "Island", "Animal Farm" },
			{ "Paradise", "Native Son" }, { "Anna Karenina(-)", "Oblomov" },
			{ "Metamorphoses", "Don Quixote" } };

	// store the users' answers and task times
	private String[] answers;
	private long[] times;

	private int currentIndex;

	private long lastTime;

	public GraphUserStudyViewer(String name, GraphData data) {

		super(name, data);

		// hide the GraphViewer's regular properties
		Property[] ps = getProperties();
		for (int i = 0; i < ps.length; i++)
			ps[i].setVisible(false);

		answers = new String[tasks.length];
		times = new long[tasks.length];
		this.currentIndex = 0;

		// and add user study properties instead

		Property<PString> idProperty = new Property<PString>("Turk ID",	new PString("Your turk id here."));
		addProperty(idProperty);

		Property<PText> questionProperty = new Property<PText>(	"Instructions",
				new PText("Two nodes are marked on the network. What is the shortest path between them?\n\n" +
						 "Pan by dragging with left mouse button down. Zoom by dragging with right mouse button down."));
		questionProperty.setReadOnly(true);
		addProperty(questionProperty);

		Property<PString> taskProperty = new Property<PString>("Task",new PString("1/" + tasks.length));
		taskProperty.setReadOnly(true);
		addProperty(taskProperty);

		Property<POptions> answerProperty = new Property<POptions>("Answer:",
				                                new POptions(new String[] { "1", "2", "3", "4", "5", "6" }));
		this.addProperty(answerProperty);

		Property<PSignal> nextProperty = new Property<PSignal>("Next",new PSignal()) {
			
			@Override
			protected boolean updating(PSignal newvalue) {

				POptions options = (POptions) getProperty("Answer:").getValue();
				answers[currentIndex] = options.options[options.selectedIndex];

				long now = new Date().getTime();
				times[currentIndex] = now - lastTime;
				lastTime = now;

				currentIndex++;

				if (currentIndex < tasks.length) {
					getProperty("Task").setValue(new PString("" + (currentIndex + 1) + "/" + tasks.length));
					showUserStudy();
				} else {
					getProperty("Next").setVisible(false);
					getProperty("Answer:").setVisible(false);
					getProperty("Task").setValue(new PString("Done"));
					Property<PString> completion = new Property<PString>("Completion Code", new PString("XYZ"));
					completion.setReadOnly(true);
					addProperty(completion);
					saveResult();
				}

				requestRender();
				return true;
			}
		};
		this.addProperty(nextProperty);
	}

	public void showUserStudy() // this centers the view between the two task
								// nodes and redraws
	{

		if (currentIndex < tasks.length) {
			
			lastTime = new Date().getTime();

			int[] c1 = this.getNodeCoordinates(tasks[currentIndex][0]);
			int[] c2 = this.getNodeCoordinates(tasks[currentIndex][1]);

			if (c1 == null || c2 == null)
				return;

			int tx = -(c1[0] + c2[0]) / 2 + 400;
			int ty = -(c1[1] + c2[1]) / 2 + 300;

			this.setTranslation(tx, ty);
			this.requestRender();
		}
	}

	@Override
	public void render(Graphics2D g) // draws rectangles over task nodes
	{
		super.render(g);

		if (currentIndex >= tasks.length)
			return;

		int[] c1 = this.getNodeCoordinates(tasks[currentIndex][0]);
		int[] c2 = this.getNodeCoordinates(tasks[currentIndex][1]);

		if (c1 == null || c2 == null)
			return;

		g.setColor(Color.red);
		g.drawRect(c1[0] - 20, c1[1] - 20, 40, 40);
		g.drawRect(c2[0] - 20, c2[1] - 20, 40, 40);
	}

	private void saveResult() // saves results from all users to a "local" tab
								// delimited results file;
	{
		try {
			String filePath = getContainer().getEnvironment().getLocalDataPath() + "/results.txt";
			String userId = ((PString) getProperty("Turk ID").getValue()).stringValue();

			FileWriter fstream = new FileWriter(new File(filePath), true);
			BufferedWriter br = new BufferedWriter(fstream);

			for (int i = 0; i < tasks.length; i++) {
				br.write(userId + "\t" + (i + 1) + "\t" + tasks[i][0] + "\t" + tasks[i][1] + "\t" + answers[i] + "\t" + times[i]);
				br.newLine();
			}

			br.close();

		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
