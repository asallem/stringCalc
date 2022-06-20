import static org.junit.Assert.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.junit.Test;

public class StringCalculator {

	@Test
	public void testOneDigit() {

		 try {
			assertEquals(8, add("8"));
		} catch (Exception e) {
			 fail("Unexpected exception was thrown");
		}
	}

	@Test
	public void testOKWithoutDelimeterOneLine() {

		 try {
			assertEquals(15, add("7,3,5"));
		} catch (Exception e) {
			 fail("Unexpected exception was thrown");
		}
	}

	@Test
	public void testOKWithoutDelimeterTwoLines() {

		 try {
			assertEquals(25, add("7,3,5\n6,4"));
		} catch (Exception e) {
			 fail("Unexpected exception was thrown");
		}
	}

	@Test
	public void testOKWithDefinedDelimeter() {

		 try {
			assertEquals(add("\\;\n2;3;5"), 10);
		} catch (Exception e) {
			 fail("Unexpected exception was thrown");
		}
	}

	@Test
	public void testEmptyString() {
		 try {
			assertEquals(add(""), 0);
		} catch (Exception e) {
			 fail("Unexpected exception was thrown");
		}
	}

	@Test
	public void testNegativeValues() {
		 try {
			add("7,-3,5\n6,-4,-7");
			fail("Should throw an Exception");
		} catch (Exception e) {
			assertEquals(e.getMessage(), "negatives not allowed[-3, -4, -7]");
		}
	}

	@Test
	public void testValuesBiggerThan1000() {
		 try {
			assertEquals(add("7,1003,5\n6,4,3333"), 22);
		} catch (Exception e) {
			 fail("Unexpected exception was thrown");
		}
	}

	@Test
	public void testMultipleDelimiter() {
		 try {
			assertEquals(add("\\[;][,][:][*]\n2;3;5\n6,4,3334:1:9*10"), 40);
		} catch (Exception e) {
			 fail("Unexpected exception was thrown");
		}
	}

	@Test
	public void testMultipleDelimiterLongerThanOneChar() {
		 try {
			assertEquals(add("\\[;][,][:][*!]\n2;3;5\n6,4,3334:1:9*!10*!5"), 45);
		} catch (Exception e) {
			 fail("Unexpected exception was thrown");
		}
	}

	 private int add(String numbers) throws Exception {

		 // empty input
		 if(numbers.length() < 1) {
			 return 0;
		 }

		 List<List<String>> numbersNestedList = extractNumbers(numbers);

		 // negative values
		 List<String> negativeValues = new ArrayList<String>();

		 // calculate sum
		 Integer result = numbersNestedList.stream().flatMap(list -> list.stream())
			.map(elt -> {
				try {  Integer num =  Integer.valueOf(elt); 
					   if ( num < 0 ) negativeValues.add(elt);
					   return num > 1000 ? 0 : num;
					// catch to ignore the first line containing delimiters
					} catch(Exception e) { return 0;}
				})
		 	.collect(Collectors.summingInt(Integer::intValue));

		 if(negativeValues.size() > 0) {
			 String msg = "negatives not allowed" + negativeValues;
			 throw new Exception(msg);
		 } else {
			 return result;
		 }

	 }

	private List<List<String>> extractNumbers(String numbers) {

		 List<String> lines = Arrays.asList(numbers.split("\\n"));
		 String delimiter = ",";
		 try {
			 Integer.valueOf(lines.get(0).split(delimiter)[0]);
		 } catch (Exception e) {
			 delimiter = lines.get(0).replace("][", "|").replace("\\", "");
		 }

		 List<List<String>> numbersNestedList = new ArrayList<List<String>>();
		 for (String strTemp : lines){
			 numbersNestedList.add((Arrays.asList(strTemp.split(delimiter))));
		 }

		return numbersNestedList;
	}
	
	
}
