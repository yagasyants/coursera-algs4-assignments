import java.awt.Color;
import java.util.LinkedList;
import java.util.List;

public class SeamCarver {
	private static final double BORDER_GRADIENT = 195075;

	private Picture picture;

	public SeamCarver(Picture picture) {
		this.picture = new Picture(picture);
	}

	public Picture picture() {
		return picture;
	}

	public int width() {
		return picture.width();
	}

	public int height() {
		return picture.height();
	}

	public double energy(int x, int y) {
		if (isOnBorder(x, y)) {
			return BORDER_GRADIENT;
		} else {
			double xGradient = getXGradient(x, y);
			double yGradient = getYGradient(x, y);

			return xGradient + yGradient;
		}
	}

	private double getXGradient(int x, int y) {
		Color xPlus1 = picture.get(x + 1, y);
		Color xMinus1 = picture.get(x - 1, y);

		return getGradient(xPlus1, xMinus1);
	}

	private boolean isOnBorder(int x, int y) {
		return x == 0 || x == picture.width() - 1 || y == 0
				|| y == picture.height() - 1;
	}

	private double getGradient(Color pixel1, Color pixel2) {
		int rX = pixel1.getRed() - pixel2.getRed();
		int gX = pixel1.getGreen() - pixel2.getGreen();
		int bX = pixel1.getBlue() - pixel2.getBlue();

		return rX * rX + gX * gX + bX * bX;
	}

	private double getYGradient(int x, int y) {
		Color yPlus1 = picture.get(x, y + 1);
		Color yMinus1 = picture.get(x, y - 1);

		return getGradient(yPlus1, yMinus1);
	}

	public int[] findHorizontalSeam() {
		Path[] currentPathes = initHorizCurrentPath();

		for (int x = picture.width() - 2; x >= 0; x--) {
			Path[] nextPathes = new Path[currentPathes.length];
			for (int y = 0; y < currentPathes.length; y++) {
				Path bestPath = findBestPath(y, currentPathes);
				Path newPath = new Path(new int[] { x, y }, bestPath);
				nextPathes[y] = newPath;
			}
			currentPathes = nextPathes;
		}

		Path shortestPath = findShortestPath(currentPathes);

		return shortestPath.getHorizontalSeam();
	}

	public int[] findVerticalSeam() {
		Path[] currentPathes = initVertCurrentPath();

		for (int y = picture.height() - 2; y >= 0; y--) {
			Path[] nextPathes = new Path[currentPathes.length];
			for (int x = 0; x < currentPathes.length; x++) {
				Path bestPath = findBestPath(x, currentPathes);
				Path newPath = new Path(new int[] { x, y }, bestPath);
				nextPathes[x] = newPath;
			}
			currentPathes = nextPathes;
		}

		Path shortestPath = findShortestPath(currentPathes);

		return shortestPath.getVerticalSeam();
	}

	private Path[] initVertCurrentPath() {
		Path[] currentPathes = new Path[picture.width()];
		for (int i = 0; i < currentPathes.length; i++) {
			currentPathes[i] = new Path(new int[] { i, picture.height() - 1 });
		}
		return currentPathes;
	}

	private Path[] initHorizCurrentPath() {
		Path[] currentPathes = new Path[picture.height()];
		for (int i = 0; i < currentPathes.length; i++) {
			currentPathes[i] = new Path(new int[] { picture.width() - 1, i });
		}
		return currentPathes;
	}

	private Path findShortestPath(Path[] currentPathes) {
		Path shortestPath = currentPathes[0];
		for (int i = 1; i < currentPathes.length; i++) {
			Path path = currentPathes[i];
			if (path.weight < shortestPath.weight) {
				shortestPath = path;
			}
		}
		return shortestPath;
	}

	private Path findBestPath(int index, Path[] currentPathes) {
		double minusOneWeight = getPathWeight(index - 1, currentPathes);
		double sameIndexWeight = getPathWeight(index, currentPathes);
		double plusOneWeight = getPathWeight(index + 1, currentPathes);
		double min = Math.min(minusOneWeight, sameIndexWeight);
		min = Math.min(min, plusOneWeight);

		if (min == minusOneWeight) {
			return currentPathes[index - 1];
		} else if (min == sameIndexWeight) {
			return currentPathes[index];
		} else {
			return currentPathes[index + 1];
		}
	}

	private double getPathWeight(int index, Path[] currentPathes) {
		if (index >= 0 && index < currentPathes.length) {
			return currentPathes[index].weight;
		} else {
			return Double.POSITIVE_INFINITY;
		}
	}

	private class Path {
		private List<int[]> vertices = new LinkedList<>();
		private double weight;

		Path(int[] vertex) {
			vertices.add(vertex);
			weight = energy(vertex[0], vertex[1]);
		}

		Path(int[] vertex, Path postfix) {
			vertices.add(vertex);
			vertices.addAll(postfix.vertices);
			weight = energy(vertex[0], vertex[1]) + postfix.weight;
		}

		int[] getVerticalSeam() {
			return getSeam(0);
		}

		int[] getHorizontalSeam() {
			return getSeam(1);
		}

		private int[] getSeam(int index) {
			int[] seam = new int[vertices.size()];
			for (int i = 0; i < seam.length; i++) {
				seam[i] = vertices.get(i)[index];
			}
			return seam;
		}
	}

	public void removeHorizontalSeam(int[] seam) {
		validateSeamLength(seam, picture.width());
		Picture newPic = new Picture(picture.width(), picture.height() - 1);
		for (int x = 0; x < picture.width(); x++) {
			int yToExclude = seam[x];
			validateIndexToRemove(x, picture.height(), seam);
			for (int y = 0; y < newPic.height(); y++) {
				int yToCopy = y;
				if (yToCopy >= yToExclude) {
					yToCopy++;
				}
				newPic.set(x, y, picture.get(x, yToCopy));
			}
		}
		picture = newPic;
	}

	public void removeVerticalSeam(int[] seam) {
		validateSeamLength(seam, picture.height());
		Picture newPic = new Picture(picture.width() - 1, picture.height());
		for (int y = 0; y < picture.height(); y++) {
			int xToExclude = seam[y];
			validateIndexToRemove(y, picture.width(), seam);
			for (int x = 0; x < newPic.width(); x++) {
				int xToCopy = x;
				if (xToCopy >= xToExclude) {
					xToCopy++;
				}
				newPic.set(x, y, picture.get(xToCopy, y));
			}
		}
		picture = newPic;
	}


	private void validateSeamLength(int[] seam, int expected) {
		if (seam == null || seam.length != expected || expected < 2) {
			throw new IllegalArgumentException(
					"seam length should be between 0 and " + expected);
		}
	}

	private void validateIndexToRemove(int index, int maxIndex, int[] seam) {
		int val = seam[index];
		if (val < 0 || val >= maxIndex) {
			throw new IndexOutOfBoundsException(
					"each value of seam should be between 0 and " + maxIndex
							+ " but was " + val);
		}

		if (index > 0 && Math.abs(val - seam[index - 1]) > 1) {
			throw new IllegalArgumentException(
					"not a seam index " + index + " is too big comparing with prev");
		}
	}

}
