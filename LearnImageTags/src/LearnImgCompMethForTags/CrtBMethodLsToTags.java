package LearnImgCompMethForTags;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import DataBase.DataBaseMethods;
import DataBase.ImageDataBaseElement;

public class CrtBMethodLsToTags {

	private Map<String, List<String>> imgAdTags;
	private Map<String, Integer> staticsForTags;
	private List<Map.Entry> sortedTags;
	private Map<String, String> descImgbyTag;
	private Map<String, String> notDescImgbyTag;
	private Map<String, ImageDataBaseElement> listFromDb;

	public CrtBMethodLsToTags() {
		System.out.println("Getting tags for DB");
		listFromDb = DataBaseMethods.getElementsFromLocaleDataBase();
		imgAdTags = new HashMap<String, List<String>>();
		staticsForTags = new HashMap<String, Integer>();

		for (Map.Entry<String, ImageDataBaseElement> entry : listFromDb
				.entrySet()) {
			imgAdTags.put(
					entry.getKey(),
					new ArrayList<String>(Arrays.asList(entry.getValue()
							.getTags().split(","))));
		}

		// System.out.println("Ids and List Of Tags");
		for (Map.Entry<String, List<String>> entry : imgAdTags.entrySet()) {
			// System.out.println(entry.getKey()+":");
			for (int i = 0; i < entry.getValue().size(); ++i) {
				// System.out.print(entry.getValue().get(i)+" , ");
				if (staticsForTags.containsKey(entry.getValue().get(i))) {
					staticsForTags.put(entry.getValue().get(i),
							staticsForTags.get(entry.getValue().get(i)) + 1);
				} else {
					staticsForTags.put(entry.getValue().get(i), 1);
				}
			}
			// System.out.println();
		}

		System.out.println("\nThe number of imgs " + imgAdTags.size() + "\n");

		// order
		List<Map.Entry> a = new ArrayList<Map.Entry>(staticsForTags.entrySet());
		Collections.sort(a, new Comparator() {
			public int compare(Object o1, Object o2) {
				Map.Entry e1 = (Map.Entry) o1;
				Map.Entry e2 = (Map.Entry) o2;
				return ((Comparable) e2.getValue()).compareTo(e1.getValue());
			}
		});

		System.out.println("\nThe statics for tags, size: " + a.size() + "\n");

		for (Map.Entry e : a) {
			System.out.println(e.getKey() + " -> " + e.getValue());
		}

		sortedTags = a;

	}

	public void selectingTagsAndCreat(int forTheFirstNrTags) {
		int i = 0;
		notDescImgbyTag = new HashMap<String, String>();
		descImgbyTag = new HashMap<String, String>();
		System.out.println("\nCreatingMatForTags...\n");
		for (Map.Entry e : sortedTags) {
			if (i < forTheFirstNrTags) {
				getListImgThatDescWithTag(e.getKey().toString());
				TagAndDescMatr tAndDescMatr = new TagAndDescMatr(e.getKey()
						.toString(), descImgbyTag, notDescImgbyTag);
				//tAndDescMatr.printMatrix();
				notDescImgbyTag.clear();
				descImgbyTag.clear();
			} else {
				break;
			}
			++i;
		}

	}

	private void getListImgThatDescWithTag(String tag) {
		int ifItsIIn = 0;
		for (Map.Entry<String, List<String>> entry : imgAdTags.entrySet()) {
			for (int i = 0; i < entry.getValue().size(); ++i) {
				if (entry.getValue().get(i).equals(tag)) {
					descImgbyTag.put(entry.getKey(),
							listFromDb.get(entry.getKey()).getPath());
					ifItsIIn = 1;
				}
			}
			if (ifItsIIn == 0) {
				notDescImgbyTag.put(entry.getKey(),
						listFromDb.get(entry.getKey()).getPath());

			}
			ifItsIIn=0;
		}
	}

}
