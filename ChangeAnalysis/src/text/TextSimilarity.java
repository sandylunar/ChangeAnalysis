package text;

import java.util.StringTokenizer;

public class TextSimilarity {

    /*
     * Quick and dirty swap of the address of 2 arrays of unsigned int
     */
    private static void swap(int[] previous, int[] next) {
	int len1 = previous.length;
	int tmp;
	for (int i = 0; i < len1; i++) {
	    tmp = previous[i];
	    previous[i] = next[i];
	    next[i] = tmp;
	}
    }

    /*
     * A function which returns how similar 2 strings are Assumes that both
     * point to 2 valid null terminated array of chars. Returns the similarity
     * between them.
     */
    public static float similarity(String[] str1, String[] str2) {
	int len1 = str1.length;
	int len2 = str2.length;
	float lenLCS;
	int j, k;
	int[] previous;
	int[] next;
	if (len1 == 0 || len2 == 0)
	    return 0;
	previous = new int[len1 + 1];
	next = new int[len1 + 1];
	for (j = 0; j < len2; ++j) {
	    for (k = 1; k <= len1; ++k) {
		if (str1[k - 1].equals(str2[j]))
		    next[k] = previous[k - 1] + 1;
		else
		    next[k] = previous[k] >= next[k - 1] ? previous[k]
			    : next[k - 1];

	    }
	    swap(previous, next);

	}
	lenLCS = (float) previous[len1];

	return lenLCS /= len1;
    }

    public static float similarity(char[] str1, char[] str2) {
	int len1 = str1.length;
	int len2 = str2.length;
	float lenLCS;
	int j, k;
	int[] previous;
	int[] next;
	if (len1 == 0 || len2 == 0)
	    return 0;
	previous = new int[len1 + 1];
	next = new int[len1 + 1];
	for (j = 0; j < len2; ++j) {
	    for (k = 1; k <= len1; ++k) {
		if (str1[k - 1] == (str2[j]))
		    next[k] = previous[k - 1] + 1;
		else
		    next[k] = previous[k] >= next[k - 1] ? previous[k]
			    : next[k - 1];

	    }
	    swap(previous, next);

	}
	lenLCS = (float) previous[len1];

	return lenLCS /= len1;
    }

    public static float editDistence(String[] str1, String[] str2) {
	int len1 = str1.length;
	int len2 = str2.length;
	float lenLCS;
	int j, k;
	int[] previous;
	int[] next;
	if (len1 == 0 || len2 == 0)
	    return 0;
	previous = new int[len1 + 1];
	next = new int[len1 + 1];
	for (j = 0; j < len2; ++j) {
	    for (k = 1; k <= len1; ++k) {
		if (str1[k - 1].equals(str2[j]))
		    next[k] = previous[k - 1] + 1;
		else
		    next[k] = previous[k] >= next[k - 1] ? previous[k]
			    : next[k - 1];

	    }
	    swap(previous, next);

	}
	lenLCS = (float) previous[len1];
	float value = ((float) len1 + (float) len2 - 2 * lenLCS)
		/ (len1 + len2);

	return value;
    }

    /*
     * Returns a pointer to the Longest Common Sequence in str1 and str2 Assumes
     * str1 and str2 point to 2 null terminated array of char
     */
    public static String LCS(String str1, String str2) {
	char[] lcs = new char[MAX_LCS];
	int i, r, c, len1 = (int) (str1.length()), len2 = (int) (str2.length());
	int[][] align;
	if (len1 == 0 || len2 == 0)
	    return null;
	align = new int[len2 + 1][len1 + 1];
	for (r = 1; r <= len2; ++r)
	    for (c = 1; c <= len1; ++c)
		if (str1.charAt(c - 1) == str1.charAt(r - 1))
		    align[r][c] = align[r - 1][c - 1] + 1;
		else
		    align[r][c] = align[r - 1][c] >= align[r][c - 1] ? align[r - 1][c]
			    : align[r][c - 1];
	for (r = len2, c = len1, i = align[r][c], lcs[i] = '\0'; i > 0 && r > 0
		&& c > 0; i = align[r][c]) {
	    if (align[r - 1][c] == i)
		--r;
	    else if (align[r][c - 1] == i)
		--c;
	    else if (align[r - 1][c - 1] == i - 1) {
		lcs[i - 1] = str1.charAt(--r);
		--c;
	    }
	}

	return String.valueOf(lcs);
    }

    public static String LCSTrimSpace(String str1, String str2) {
	StringTokenizer st1 = new StringTokenizer(str1);
	StringTokenizer st2 = new StringTokenizer(str2);
	StringBuilder s1 = new StringBuilder();
	StringBuilder s2 = new StringBuilder();
	while (st1.hasMoreTokens())
	    s1.append(st1.nextToken() + " ");
	while (st2.hasMoreTokens())
	    s2.append(st2.nextToken() + " ");
	return LCS(s1.toString().trim(), s2.toString().trim());
    }

    public static float editDistence(String str1, String str2) {
	return editDistence(str1.toLowerCase().split("[^a-zA-Z0-9]"), str2
		.toLowerCase().split("[^a-zA-Z0-9]"));
    }

    public static float similarity(String str1, String str2, int i) {
	return similarity(str1.toLowerCase().split("[^a-zA-Z0-9]"), str2
		.toLowerCase().split("[^a-zA-Z0-9]"));
    }

    static float similarity(String string, String string2) {
	// TODO Auto-generated method stub
	StringTokenizer st = new StringTokenizer(string);
	StringTokenizer st2 = new StringTokenizer(string2);
	String[] s1 = new String[st.countTokens()];
	String[] s2 = new String[st2.countTokens()];

	int i = 0;
	while (st.hasMoreTokens()) {
	    s1[i] = st.nextToken().toLowerCase();
	    i++;
	}
	i = 0;
	while (st2.hasMoreTokens()) {
	    s2[i] = st2.nextToken().toLowerCase();
	    i++;
	}
	return similarity(s1, s2);
    }

    public static void main(String[] args) {
	String[] test1 = {
		"                           listeners         to   listeners",
		"create a new applet object",
		"Gets the Locale for the applet , if it has been set" };
	String[] test2 = {
		"                                         listeners",
		"Constructs a new Applet",
		"It allows the applet to maintain its own locale separated from the locale" };

	for (int i = 0; i < test1.length; i++) {
	    float sim = similarity(test1[i], test2[i]);
	    System.out.println(test1[i] + "<--->" + test2[i] + " = " + sim);
	    System.out.println(test1[i] + "<LCS>" + test2[i] + " = "
		    + TextSimilarity.LCSTrimSpace(test1[i], test2[i]));
	}

	for (int i = 0; i < test1.length; i++) {
	    float sim = editDistence(test1[i], test2[i]);
	    System.out.println(test1[i] + "<--->" + test2[i] + " = " + sim);
	}
    }

    public static final int MAX_LCS = 100000;
   
    public static float getSimilarity(String str1, String str2) {
	    char[] ch1 = str1.trim().toCharArray();
	    char[] ch2 = str2.trim().toCharArray();
	    float sim = similarity(ch1, ch2);
	    return sim;
    }
}
