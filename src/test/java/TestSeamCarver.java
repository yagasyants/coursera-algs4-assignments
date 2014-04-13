import static org.junit.Assert.*;

import java.awt.Color;

import org.junit.Test;

public class TestSeamCarver {
	@Test
	public void testEnergy() {
		Picture picture = createEnergyCompPicture();
		SeamCarver seamCarver = new SeamCarver(picture);
		double energyX1Y2 = seamCarver.energy(1, 2);
		double energyX1Y1 = seamCarver.energy(1, 1);
		double energyX0Y0 = seamCarver.energy(0, 0);
		double energyX2Y1 = seamCarver.energy(2, 1);

		assertEquals(52024, energyX1Y2, 0);
		assertEquals(52225, energyX1Y1, 0);
		assertEquals(195075, energyX0Y0, 0);
		assertEquals(195075, energyX2Y1, 0);
	}

	@Test
	public void testVerticalSeam() {
		Picture picture = createEnergyCompPicture();
		SeamCarver seamCarver = new SeamCarver(picture);
		int[] vertSeam = seamCarver.findVerticalSeam();

		assertEquals(1, vertSeam[1]);
		assertEquals(1, vertSeam[2]);
	}

	@Test
	public void testHorizontalSeam() {
		Picture picture = createEnergyCompPicture();
		SeamCarver seamCarver = new SeamCarver(picture);
		int[] vertSeam = seamCarver.findHorizontalSeam();

		assertEquals(1, vertSeam[0]);
		assertEquals(2, vertSeam[1]);
		assertEquals(1, vertSeam[2]);
	}

	@Test
	public void testVerticalBigPicSeam() {
		Picture picture = createSeamCompPicture();
		SeamCarver seamCarver = new SeamCarver(picture);
		int[] vertSeam = seamCarver.findVerticalSeam();

		assertEquals(2, vertSeam[0]);
		assertEquals(3, vertSeam[1]);
		assertEquals(3, vertSeam[2]);
		assertEquals(3, vertSeam[3]);
		assertEquals(2, vertSeam[4]);
	}

	@Test
	public void testRemoveVertSeam() {
		Picture picture = createEnergyCompPicture();
		SeamCarver seamCarver = new SeamCarver(picture);
		int[] vertSeam = seamCarver.findVerticalSeam();
		seamCarver.removeVerticalSeam(vertSeam);
		int[] newVertSeam = seamCarver.findVerticalSeam();

		assertEquals(4, seamCarver.height());
		assertEquals(2, seamCarver.width());

		assertEquals(0, newVertSeam[0]);
		assertEquals(0, newVertSeam[1]);
		assertEquals(0, newVertSeam[2]);
	}

	@Test
	public void testRemoveHorizSeam() {
		Picture picture = createEnergyCompPicture();
		SeamCarver seamCarver = new SeamCarver(picture);
		int[] horizSeam = seamCarver.findHorizontalSeam();
		seamCarver.removeHorizontalSeam(horizSeam);
		int[] newHorizSeam = seamCarver.findHorizontalSeam();

		assertEquals(3, seamCarver.height());
		assertEquals(3, seamCarver.width());

		assertEquals(0, newHorizSeam[0]);
		assertEquals(1, newHorizSeam[1]);
		assertEquals(0, newHorizSeam[2]);
	}

	@Test(expected = IndexOutOfBoundsException.class)
	public void testRemoveVertSeamOutOfBounds() {
		Picture picture = createEnergyCompPicture();
		SeamCarver seamCarver = new SeamCarver(picture);
		int[] vertSeam = { -1, 0, 0, 0 };
		seamCarver.removeVerticalSeam(vertSeam);
	}

	@Test(expected = IndexOutOfBoundsException.class)
	public void testRemoveVertSeamOutOfUpperBounds() {
		Picture picture = createEnergyCompPicture();
		SeamCarver seamCarver = new SeamCarver(picture);
		int[] vertSeam = { 3, 0, 0, 0 };
		seamCarver.removeVerticalSeam(vertSeam);
	}

	@Test(expected = IndexOutOfBoundsException.class)
	public void testRemoveHorizSeamOutOfBounds() {
		Picture picture = createEnergyCompPicture();
		SeamCarver seamCarver = new SeamCarver(picture);
		int[] seam = { -1, 0, 0 };
		seamCarver.removeHorizontalSeam(seam);
	}

	@Test(expected = IndexOutOfBoundsException.class)
	public void testRemoveHorizSeamOutOfUpperBounds() {
		Picture picture = createEnergyCompPicture();
		SeamCarver seamCarver = new SeamCarver(picture);
		int[] seam = { 4, 0, 0 };
		seamCarver.removeHorizontalSeam(seam);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testRemoveVertSeamTooLongArray() {
		Picture picture = createEnergyCompPicture();
		SeamCarver seamCarver = new SeamCarver(picture);
		int[] vertSeam = { 0, 0, 0, 0, 0 };
		seamCarver.removeVerticalSeam(vertSeam);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testRemoveHorizSeamTooSmallSeam() {
		Picture picture = createEnergyCompPicture();
		SeamCarver seamCarver = new SeamCarver(picture);
		int[] seam = { 0, 0 };
		seamCarver.removeHorizontalSeam(seam);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testRemoveHorizSeamNotSeam() {
		Picture picture = createEnergyCompPicture();
		SeamCarver seamCarver = new SeamCarver(picture);
		int[] seam = { 0, 2, 0 };
		seamCarver.removeHorizontalSeam(seam);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testRemoveHorizSeamWhenTooSmall() {
		Picture picture = createEnergyCompPicture();
		SeamCarver seamCarver = new SeamCarver(picture);
		int[] seam = { 0, 0, 0 };
		seamCarver.removeHorizontalSeam(seam);
		seamCarver.removeHorizontalSeam(seam);
		seamCarver.removeHorizontalSeam(seam);
		seamCarver.removeHorizontalSeam(seam);
	}


	private Picture createEnergyCompPicture() {
		Picture picture = new Picture(3, 4);

		picture.set(0, 0, new Color(255, 101, 51));
		picture.set(1, 0, new Color(255, 101, 153));
		picture.set(2, 0, new Color(255, 101, 255));

		picture.set(0, 1, new Color(255, 153, 51));
		picture.set(1, 1, new Color(255, 153, 153));
		picture.set(2, 1, new Color(255, 153, 255));

		picture.set(0, 2, new Color(255, 203, 51));
		picture.set(1, 2, new Color(255, 204, 153));
		picture.set(2, 2, new Color(255, 205, 255));

		picture.set(0, 3, new Color(255, 255, 51));
		picture.set(1, 3, new Color(255, 255, 153));
		picture.set(2, 3, new Color(255, 255, 255));

		return picture;
	}

	private Picture createSeamCompPicture() {
		Picture picture = new Picture(6, 5);

		picture.set(0, 0, new Color(97, 82, 107));
		picture.set(1, 0, new Color(220, 172, 141));
		picture.set(2, 0, new Color(243, 71, 205));
		picture.set(3, 0, new Color(129, 173, 222));
		picture.set(4, 0, new Color(225, 40, 209));
		picture.set(5, 0, new Color(66, 109, 219));

		picture.set(0, 1, new Color(181, 78, 68));
		picture.set(1, 1, new Color(15, 28, 216));
		picture.set(2, 1, new Color(245, 150, 150));
		picture.set(3, 1, new Color(177, 100, 167));
		picture.set(4, 1, new Color(205, 205, 177));
		picture.set(5, 1, new Color(147, 58, 99));

		picture.set(0, 2, new Color(196, 224, 21));
		picture.set(1, 2, new Color(166, 217, 190));
		picture.set(2, 2, new Color(128, 120, 162));
		picture.set(3, 2, new Color(104, 59, 110));
		picture.set(4, 2, new Color(49, 148, 137));
		picture.set(5, 2, new Color(192, 101, 89));

		picture.set(0, 3, new Color(83, 143, 103));
		picture.set(1, 3, new Color(110, 79, 247));
		picture.set(2, 3, new Color(106, 71, 174));
		picture.set(3, 3, new Color(92, 240, 205));
		picture.set(4, 3, new Color(129, 56, 146));
		picture.set(5, 3, new Color(121, 111, 147));

		picture.set(0, 4, new Color(82, 157, 137));
		picture.set(1, 4, new Color(92, 110, 129));
		picture.set(2, 4, new Color(183, 107, 80));
		picture.set(3, 4, new Color(89, 24, 217));
		picture.set(4, 4, new Color(207, 69, 32));
		picture.set(5, 4, new Color(156, 112, 31));

		return picture;
	}

}
