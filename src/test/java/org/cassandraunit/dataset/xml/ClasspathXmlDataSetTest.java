package org.cassandraunit.dataset.xml;

import static org.cassandraunit.SampleDataSetChecker.assertThatKeyspaceModelWithCompositeTypeIsOk;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.hamcrest.Matchers.nullValue;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;
import me.prettyprint.hector.api.ddl.ColumnIndexType;
import me.prettyprint.hector.api.ddl.ColumnType;
import me.prettyprint.hector.api.ddl.ComparatorType;

import org.apache.commons.lang.StringUtils;
import org.cassandraunit.dataset.DataSet;
import org.cassandraunit.dataset.ParseException;
import org.cassandraunit.model.ColumnFamilyModel;
import org.cassandraunit.model.RowModel;
import org.cassandraunit.model.StrategyModel;
import org.cassandraunit.type.GenericTypeEnum;
import org.junit.Test;

/**
 * 
 * @author Jeremy Sevellec
 * 
 */
public class ClasspathXmlDataSetTest {

	@Test
	public void shouldGetAXmlDataSet() {

		DataSet dataSet = new ClassPathXmlDataSet("xml/datasetDefaultValues.xml");
		assertThat(dataSet, notNullValue());
	}

	@Test
	public void shouldNotGetAXmlDataSetBecauseNull() {
		try {
			DataSet dataSet = new ClassPathXmlDataSet(null);
			fail();
		} catch (ParseException e) {
			/* nothing to do, it what we want */
		}
	}

	@Test
	public void shouldNotGetAXmlDataSetBecauseItNotExist() {
		try {
			DataSet dataSet = new ClassPathXmlDataSet("xml/unknownDataSet.xml");
			fail();
		} catch (ParseException e) {
			/* nothing to do, it what we want */
		}
	}

	@Test
	public void shouldNotGetAXmlDataSetBecauseItIsInvalid() {
		try {
			DataSet dataSet = new ClassPathXmlDataSet("xml/invalidDataSet.xml");
			dataSet.getKeyspace();
			fail();
		} catch (ParseException e) {
			/* nothing to do, it what we want */
			assertThat(StringUtils.contains(e.getMessage(),
					"Invalid content was found starting with element 'columnFamily'"), is(Boolean.TRUE));
		}
	}

	@Test
	public void shouldGetKeyspaceWithDefaultValues() {

		DataSet dataSet = new ClassPathXmlDataSet("xml/datasetDefaultValues.xml");
		assertThat(dataSet.getKeyspace(), notNullValue());
		assertThat(dataSet.getKeyspace().getName(), notNullValue());
		assertThat(dataSet.getKeyspace().getName(), is("beautifulKeyspaceName"));
		assertThat(dataSet.getKeyspace().getReplicationFactor(), is(1));
		assertThat(dataSet.getKeyspace().getStrategy(), is(StrategyModel.SIMPLE_STRATEGY));

	}

	@Test
	public void shouldGetKeyspaceWithDefinedValues() {
		DataSet dataSet = new ClassPathXmlDataSet("xml/datasetDefinedValues.xml");
		assertThat(dataSet.getKeyspace(), notNullValue());
		assertThat(dataSet.getKeyspace().getName(), notNullValue());
		assertThat(dataSet.getKeyspace().getName(), is("otherKeyspaceName"));
		assertThat(dataSet.getKeyspace().getReplicationFactor(), is(2));
		assertThat(dataSet.getKeyspace().getStrategy(), is(StrategyModel.SIMPLE_STRATEGY));
	}

	@Test
	public void shouldGetOneColumnFamilyWithDefaultValues() {
		DataSet dataSet = new ClassPathXmlDataSet("xml/datasetDefaultValues.xml");
		assertThat(dataSet.getColumnFamilies(), notNullValue());
		assertThat(dataSet.getColumnFamilies().isEmpty(), is(false));
		assertThat(dataSet.getColumnFamilies().get(0), notNullValue());
		assertThat(dataSet.getColumnFamilies().get(0).getName(), is("columnFamily1"));
		assertThat(dataSet.getColumnFamilies().get(0).getType(), is(ColumnType.STANDARD));
		assertThat(dataSet.getColumnFamilies().get(0).getKeyType().getClassName(),
				is(ComparatorType.BYTESTYPE.getClassName()));
		assertThat(dataSet.getColumnFamilies().get(0).getComparatorType().getClassName(),
				is(ComparatorType.BYTESTYPE.getClassName()));
		assertThat(dataSet.getColumnFamilies().get(0).getSubComparatorType(), nullValue());
		assertThat(dataSet.getColumnFamilies().get(0).getDefaultColumnValueType(), nullValue());

	}

	@Test
	public void shouldGetColumnFamiliesWithDefinedValues() {
		DataSet dataSet = new ClassPathXmlDataSet("xml/datasetDefinedValues.xml");
		assertThat(dataSet.getColumnFamilies(), notNullValue());
		assertThat(dataSet.getColumnFamilies().isEmpty(), is(false));
        ColumnFamilyModel beautifulColumnFamily = dataSet.getColumnFamilies().get(0);
        assertThat(beautifulColumnFamily, notNullValue());
		assertThat(beautifulColumnFamily.getName(), is("beautifulColumnFamilyName"));
		assertThat(beautifulColumnFamily.getType(), is(ColumnType.SUPER));
		assertThat(beautifulColumnFamily.getKeyType().getClassName(),
				is(ComparatorType.TIMEUUIDTYPE.getClassName()));
		assertThat(beautifulColumnFamily.getComparatorType().getClassName(),
				is(ComparatorType.UTF8TYPE.getClassName()));
		assertThat(beautifulColumnFamily.getSubComparatorType().getClassName(),
				is(ComparatorType.LONGTYPE.getClassName()));
		assertThat(beautifulColumnFamily.getDefaultColumnValueType().getClassName(),
				is(ComparatorType.UTF8TYPE.getClassName()));
        assertThat(beautifulColumnFamily.getComment(),is("amazing comment"));
        assertThat(beautifulColumnFamily.getCompactionStrategy(),is("LeveledCompactionStrategy"));
        assertThat(beautifulColumnFamily.getCompactionStrategyOptions().get(0).getName(),is("sstable_size_in_mb"));
        assertThat(beautifulColumnFamily.getCompactionStrategyOptions().get(0).getValue(),is("10"));
        assertThat(beautifulColumnFamily.getGcGraceSeconds(),is(9999));
        assertThat(beautifulColumnFamily.getMaxCompactionThreshold(),is(31));
        assertThat(beautifulColumnFamily.getMinCompactionThreshold(),is(3));
        assertThat(beautifulColumnFamily.getReadRepairChance(),is(0.1d));
        assertThat(beautifulColumnFamily.getReplicationOnWrite(),is(Boolean.FALSE));

		assertThat(dataSet.getColumnFamilies().get(1).getName(), is("amazingColumnFamilyName"));
		assertThat(dataSet.getColumnFamilies().get(1).getType(), is(ColumnType.STANDARD));
		assertThat(dataSet.getColumnFamilies().get(1).getKeyType().getClassName(),
				is(ComparatorType.UTF8TYPE.getClassName()));
		assertThat(dataSet.getColumnFamilies().get(1).getComparatorType().getClassName(),
				is(ComparatorType.UTF8TYPE.getClassName()));
	}

	@Test
	public void shouldGetOneStandardColumnFamilyDataWithDefaultValues() {
		DataSet dataSet = new ClassPathXmlDataSet("xml/datasetDefaultValues.xml");
		assertThat(dataSet.getColumnFamilies().get(0).getRows(), notNullValue());
		assertThat(dataSet.getColumnFamilies().get(0).getRows().size(), is(3));
		verifyStandardRow(dataSet.getColumnFamilies().get(0).getRows().get(0), "10", 2, "11", "11", "12", "12");
		verifyStandardRow(dataSet.getColumnFamilies().get(0).getRows().get(1), "20", 3, "21", "21", "22", "22");
		verifyStandardRow(dataSet.getColumnFamilies().get(0).getRows().get(2), "30", 2, "31", "31", "32", "32");
	}

	private void verifyStandardRow(RowModel row, String expectedRowkey, int expectedSize,
			String expectedFirstColumnName, String expectedFirstColumnValue, String expectedSecondColumnName,
			String expectedSecondColumnValue) {
		assertThat(row, notNullValue());
		assertThat(row.getKey().toString(), is(expectedRowkey));
		assertThat(row.getSuperColumns(), notNullValue());
		assertThat(row.getSuperColumns().isEmpty(), is(true));
		assertThat(row.getColumns(), notNullValue());
		assertThat(row.getColumns().size(), is(expectedSize));
		assertThat(row.getColumns().get(0), notNullValue());
		assertThat(row.getColumns().get(0).getName().toString(), is(expectedFirstColumnName));
		assertThat(row.getColumns().get(0).getValue().toString(), is(expectedFirstColumnValue));
		assertThat(row.getColumns().get(0), notNullValue());
		assertThat(row.getColumns().get(1).getName().toString(), is(expectedSecondColumnName));
		assertThat(row.getColumns().get(1).getValue().toString(), is(expectedSecondColumnValue));
	}

	@Test
	public void shouldGetOneSuperColumnFamilyData() {
		DataSet dataSet = new ClassPathXmlDataSet("xml/datasetDefinedValues.xml");
		assertThat(dataSet.getColumnFamilies().get(0).getRows(), notNullValue());
		assertThat(dataSet.getColumnFamilies().get(0).getRows().size(), is(2));
		RowModel row1 = dataSet.getColumnFamilies().get(0).getRows().get(0);
		assertThat(row1, notNullValue());
		assertThat(row1.getKey().toString(), is("13816710-1dd2-11b2-879a-782bcb80ff6a"));
		assertThat(row1.getColumns(), notNullValue());
		assertThat(row1.getColumns().isEmpty(), is(true));

		assertThat(row1.getSuperColumns(), notNullValue());
		assertThat(row1.getSuperColumns().size(), is(2));
		assertThat(row1.getSuperColumns().get(0), notNullValue());
		assertThat(row1.getSuperColumns().get(0).getName().toString(), is("name11"));
		assertThat(row1.getSuperColumns().get(0).getColumns(), notNullValue());
		assertThat(row1.getSuperColumns().get(0).getColumns().size(), is(2));
		assertThat(row1.getSuperColumns().get(0).getColumns().get(0), notNullValue());
		assertThat(row1.getSuperColumns().get(0).getColumns().get(0).getName().toString(), is("111"));
		assertThat(row1.getSuperColumns().get(0).getColumns().get(0).getValue().toString(), is("value111"));
		assertThat(row1.getSuperColumns().get(0).getColumns().get(1), notNullValue());
		assertThat(row1.getSuperColumns().get(0).getColumns().get(1).getName().toString(), is("112"));
		assertThat(row1.getSuperColumns().get(0).getColumns().get(1).getValue().toString(), is("value112"));

		assertThat(row1.getSuperColumns().get(1).getName().toString(), is("name12"));
		assertThat(row1.getSuperColumns().get(1).getColumns(), notNullValue());
		assertThat(row1.getSuperColumns().get(1).getColumns().size(), is(2));
		assertThat(row1.getSuperColumns().get(1).getColumns().get(0), notNullValue());
		assertThat(row1.getSuperColumns().get(1).getColumns().get(0).getName().toString(), is("121"));
		assertThat(row1.getSuperColumns().get(1).getColumns().get(0).getValue().toString(), is("value121"));
		assertThat(row1.getSuperColumns().get(1).getColumns().get(1), notNullValue());
		assertThat(row1.getSuperColumns().get(1).getColumns().get(1).getName().toString(), is("122"));
		assertThat(row1.getSuperColumns().get(1).getColumns().get(1).getValue().toString(), is("value122"));

		RowModel row2 = dataSet.getColumnFamilies().get(0).getRows().get(1);
		assertThat(row2, notNullValue());
		assertThat(row2.getKey().toString(), is("13818e20-1dd2-11b2-879a-782bcb80ff6a"));
		assertThat(row2.getColumns(), notNullValue());
		assertThat(row2.getColumns().isEmpty(), is(true));

		assertThat(row2.getSuperColumns(), notNullValue());
		assertThat(row2.getSuperColumns().size(), is(3));
		assertThat(row2.getSuperColumns().get(0), notNullValue());
		assertThat(row2.getSuperColumns().get(0).getName().toString(), is("name21"));
		assertThat(row2.getSuperColumns().get(0).getColumns(), notNullValue());
		assertThat(row2.getSuperColumns().get(0).getColumns().size(), is(2));
		assertThat(row2.getSuperColumns().get(0).getColumns().get(0), notNullValue());
		assertThat(row2.getSuperColumns().get(0).getColumns().get(0).getName().toString(), is("211"));
		assertThat(row2.getSuperColumns().get(0).getColumns().get(0).getValue().toString(), is("value211"));
		assertThat(row2.getSuperColumns().get(0).getColumns().get(1), notNullValue());
		assertThat(row2.getSuperColumns().get(0).getColumns().get(1).getName().toString(), is("212"));
		assertThat(row2.getSuperColumns().get(0).getColumns().get(1).getValue().toString(), is("value212"));

		assertThat(row2.getSuperColumns().get(1).getName().toString(), is("name22"));
		assertThat(row2.getSuperColumns().get(1).getColumns(), notNullValue());
		assertThat(row2.getSuperColumns().get(1).getColumns().size(), is(2));
		assertThat(row2.getSuperColumns().get(1).getColumns().get(0), notNullValue());
		assertThat(row2.getSuperColumns().get(1).getColumns().get(0).getName().toString(), is("221"));
		assertThat(row2.getSuperColumns().get(1).getColumns().get(0).getValue().toString(), is("value221"));
		assertThat(row2.getSuperColumns().get(1).getColumns().get(1), notNullValue());
		assertThat(row2.getSuperColumns().get(1).getColumns().get(1).getName().toString(), is("222"));
		assertThat(row2.getSuperColumns().get(1).getColumns().get(1).getValue().toString(), is("value222"));

		assertThat(row2.getSuperColumns().get(2).getName().toString(), is("name23"));
		assertThat(row2.getSuperColumns().get(2).getColumns(), notNullValue());
		assertThat(row2.getSuperColumns().get(2).getColumns().size(), is(1));
		assertThat(row2.getSuperColumns().get(2).getColumns().get(0), notNullValue());
		assertThat(row2.getSuperColumns().get(2).getColumns().get(0).getName().toString(), is("231"));
		assertThat(row2.getSuperColumns().get(2).getColumns().get(0).getValue().toString(), is("value231"));
	}

	@Test
	public void shouldGetDefaultBytesTypeForColumnValue() throws Exception {
		DataSet dataSet = new ClassPathXmlDataSet("xml/datasetColumnValueTest.xml");
		assertThat(dataSet.getColumnFamilies().get(0), notNullValue());
		assertThat(dataSet.getColumnFamilies().get(0).getName(), is("beautifulColumnFamilyName"));
		assertThat(dataSet.getColumnFamilies().get(0).getRows(), notNullValue());
		assertThat(dataSet.getColumnFamilies().get(0).getRows().get(0), notNullValue());
		assertThat(dataSet.getColumnFamilies().get(0).getRows().get(0).getColumns(), notNullValue());
		assertThat(dataSet.getColumnFamilies().get(0).getRows().get(0).getColumns().get(0), notNullValue());
		assertThat(dataSet.getColumnFamilies().get(0).getRows().get(0).getColumns().get(0).getValue().toString(),
				is("11"));
		assertThat(dataSet.getColumnFamilies().get(0).getRows().get(0).getColumns().get(0).getValue().getType(),
				is(GenericTypeEnum.BYTES_TYPE));

	}

	@Test
	public void shouldGetDefaultUTF8TypeForColumnValue() throws Exception {
		DataSet dataSet = new ClassPathXmlDataSet("xml/datasetColumnValueTest.xml");
		assertThat(dataSet.getColumnFamilies().get(1), notNullValue());
		assertThat(dataSet.getColumnFamilies().get(1).getName(), is("beautifulColumnFamilyName2"));
		assertThat(dataSet.getColumnFamilies().get(1).getRows(), notNullValue());
		assertThat(dataSet.getColumnFamilies().get(1).getRows().get(0), notNullValue());
		assertThat(dataSet.getColumnFamilies().get(1).getRows().get(0).getColumns(), notNullValue());
		assertThat(dataSet.getColumnFamilies().get(1).getRows().get(0).getColumns().get(0), notNullValue());
		assertThat(dataSet.getColumnFamilies().get(1).getRows().get(0).getColumns().get(0).getValue().toString(),
				is("11"));
		assertThat(dataSet.getColumnFamilies().get(1).getRows().get(0).getColumns().get(0).getValue().getType(),
				is(GenericTypeEnum.UTF_8_TYPE));
	}

	@Test
	public void shouldGetDefaultUTF8TypeAndDefinedLongTypeForColumnValue() throws Exception {
		DataSet dataSet = new ClassPathXmlDataSet("xml/datasetColumnValueTest.xml");
		assertThat(dataSet.getColumnFamilies().get(2), notNullValue());
		assertThat(dataSet.getColumnFamilies().get(2).getName(), is("beautifulColumnFamilyName3"));
		assertThat(dataSet.getColumnFamilies().get(2).getRows(), notNullValue());
		assertThat(dataSet.getColumnFamilies().get(2).getRows().get(0), notNullValue());
		assertThat(dataSet.getColumnFamilies().get(2).getRows().get(0).getColumns(), notNullValue());
		assertThat(dataSet.getColumnFamilies().get(2).getRows().get(0).getColumns().get(0), notNullValue());
		assertThat(dataSet.getColumnFamilies().get(2).getRows().get(0).getColumns().get(0).getValue().toString(),
				is("1"));
		assertThat(dataSet.getColumnFamilies().get(2).getRows().get(0).getColumns().get(0).getValue().getType(),
				is(GenericTypeEnum.LONG_TYPE));

		assertThat(dataSet.getColumnFamilies().get(2).getRows().get(0).getColumns(), notNullValue());
		assertThat(dataSet.getColumnFamilies().get(2).getRows().get(0).getColumns().get(1), notNullValue());
		assertThat(dataSet.getColumnFamilies().get(2).getRows().get(0).getColumns().get(1).getValue().toString(),
				is("value12"));
		assertThat(dataSet.getColumnFamilies().get(2).getRows().get(0).getColumns().get(1).getValue().getType(),
				is(GenericTypeEnum.UTF_8_TYPE));
	}

	@Test
	public void shouldGetCounterStandardColumnFamily() {
		DataSet dataSet = new ClassPathXmlDataSet("xml/datasetDefinedValues.xml");
		assertThat(dataSet.getColumnFamilies().get(2).getName(), is("counterStandardColumnFamilyName"));
		assertThat(dataSet.getColumnFamilies().get(2).getType(), is(ColumnType.STANDARD));
		assertThat(dataSet.getColumnFamilies().get(2).getKeyType().getClassName(),
				is(ComparatorType.LONGTYPE.getClassName()));
		assertThat(dataSet.getColumnFamilies().get(2).getKeyType().getClassName(),
				is(ComparatorType.LONGTYPE.getClassName()));
		assertThat(dataSet.getColumnFamilies().get(2).getComparatorType().getClassName(),
				is(ComparatorType.UTF8TYPE.getClassName()));
		assertThat(dataSet.getColumnFamilies().get(2).getDefaultColumnValueType().getClassName(),
				is(ComparatorType.COUNTERTYPE.getClassName()));
		assertThat(dataSet.getColumnFamilies().get(2).getRows(), notNullValue());
		assertThat(dataSet.getColumnFamilies().get(2).getRows().size(), is(1));
		assertThat(dataSet.getColumnFamilies().get(2).getRows().get(0), notNullValue());
		assertThat(dataSet.getColumnFamilies().get(2).getRows().get(0).getColumns(), notNullValue());
		assertThat(dataSet.getColumnFamilies().get(2).getRows().get(0).getColumns().size(), is(2));
		assertThat(dataSet.getColumnFamilies().get(2).getRows().get(0).getColumns().get(0), notNullValue());
		assertThat(dataSet.getColumnFamilies().get(2).getRows().get(0).getColumns().get(0).getName().getValue(),
				is("counter11"));
		assertThat(dataSet.getColumnFamilies().get(2).getRows().get(0).getColumns().get(0).getValue().getValue(),
				is("11"));
		assertThat(dataSet.getColumnFamilies().get(2).getRows().get(0).getColumns().get(0).getValue().getType(),
				is(GenericTypeEnum.COUNTER_TYPE));
		assertThat(dataSet.getColumnFamilies().get(2).getRows().get(0).getColumns().get(1), notNullValue());
		assertThat(dataSet.getColumnFamilies().get(2).getRows().get(0).getColumns().get(1).getName().getValue(),
				is("counter12"));
		assertThat(dataSet.getColumnFamilies().get(2).getRows().get(0).getColumns().get(1).getValue().getValue(),
				is("12"));
		assertThat(dataSet.getColumnFamilies().get(2).getRows().get(0).getColumns().get(1).getValue().getType(),
				is(GenericTypeEnum.COUNTER_TYPE));
	}

	@Test
	public void shouldGetCounterSuperColumnFamily() {
		DataSet dataSet = new ClassPathXmlDataSet("xml/datasetDefinedValues.xml");
		assertThat(dataSet.getColumnFamilies().get(3).getName(), is("counterSuperColumnFamilyName"));
		assertThat(dataSet.getColumnFamilies().get(3).getType(), is(ColumnType.SUPER));
		assertThat(dataSet.getColumnFamilies().get(3).getKeyType().getClassName(),
				is(ComparatorType.LONGTYPE.getClassName()));
		assertThat(dataSet.getColumnFamilies().get(3).getComparatorType().getClassName(),
				is(ComparatorType.UTF8TYPE.getClassName()));
		assertThat(dataSet.getColumnFamilies().get(3).getDefaultColumnValueType().getClassName(),
				is(ComparatorType.COUNTERTYPE.getClassName()));

		assertThat(dataSet.getColumnFamilies().get(3).getRows(), notNullValue());
		assertThat(dataSet.getColumnFamilies().get(3).getRows().size(), is(1));
		assertThat(dataSet.getColumnFamilies().get(3).getRows().get(0), notNullValue());
		assertThat(dataSet.getColumnFamilies().get(3).getRows().get(0).getSuperColumns(), notNullValue());
		assertThat(dataSet.getColumnFamilies().get(3).getRows().get(0).getSuperColumns().size(), is(1));
		assertThat(dataSet.getColumnFamilies().get(3).getRows().get(0).getSuperColumns().get(0), notNullValue());
		assertThat(dataSet.getColumnFamilies().get(3).getRows().get(0).getSuperColumns().get(0).getName().getValue(),
				is("counter11"));
		assertThat(dataSet.getColumnFamilies().get(3).getRows().get(0).getSuperColumns().get(0).getColumns(),
				notNullValue());
		assertThat(dataSet.getColumnFamilies().get(3).getRows().get(0).getSuperColumns().get(0).getColumns().size(),
				is(2));
		assertThat(dataSet.getColumnFamilies().get(3).getRows().get(0).getSuperColumns().get(0).getColumns().get(0),
				notNullValue());
		assertThat(dataSet.getColumnFamilies().get(3).getRows().get(0).getSuperColumns().get(0).getColumns().get(0)
				.getName().getValue(), is("counter111"));
		assertThat(dataSet.getColumnFamilies().get(3).getRows().get(0).getSuperColumns().get(0).getColumns().get(0)
				.getValue().getValue(), is("111"));
		assertThat(dataSet.getColumnFamilies().get(3).getRows().get(0).getSuperColumns().get(0).getColumns().get(0)
				.getValue().getType(), is(GenericTypeEnum.COUNTER_TYPE));
		assertThat(dataSet.getColumnFamilies().get(3).getRows().get(0).getSuperColumns().get(0).getColumns().get(1),
				notNullValue());
		assertThat(dataSet.getColumnFamilies().get(3).getRows().get(0).getSuperColumns().get(0).getColumns().get(1)
				.getName().getValue(), is("counter112"));
		assertThat(dataSet.getColumnFamilies().get(3).getRows().get(0).getSuperColumns().get(0).getColumns().get(1)
				.getValue().getValue(), is("112"));
		assertThat(dataSet.getColumnFamilies().get(3).getRows().get(0).getSuperColumns().get(0).getColumns().get(1)
				.getValue().getType(), is(GenericTypeEnum.COUNTER_TYPE));

	}

	@Test(expected = ParseException.class)
	public void shouldNotGetCounterColumnFamilyBecauseThereIsFunctionOverridingDefaultValueType() {
		DataSet dataSet = new ClassPathXmlDataSet("xml/dataSetBadCounterColumnFamilyWithFunction.xml");
		dataSet.getKeyspace();
	}

	@Test
	public void shouldGetAColumnFamilyWithSecondaryIndex() {
		DataSet dataSet = new ClassPathXmlDataSet("xml/datasetWithSecondaryIndex.xml");
		assertThat(dataSet.getColumnFamilies().get(0).getColumnsMetadata().get(0).getColumnName(),
				is("columnWithIndexAndUTF8ValidationClass"));
		assertThat(dataSet.getColumnFamilies().get(0).getColumnsMetadata().get(0).getColumnIndexType(),
				is(ColumnIndexType.KEYS));
		assertThat(dataSet.getColumnFamilies().get(0).getColumnsMetadata().get(0).getValidationClass(),
				is(ComparatorType.UTF8TYPE));

        assertThat(dataSet.getColumnFamilies().get(0).getColumnsMetadata().get(1).getColumnName(),
                is("columnWithIndexAndIndexNameAndUTF8ValidationClass"));
        assertThat(dataSet.getColumnFamilies().get(0).getColumnsMetadata().get(1).getColumnIndexType(),
                is(ColumnIndexType.KEYS));
        assertThat(dataSet.getColumnFamilies().get(0).getColumnsMetadata().get(1).getValidationClass(),
                is(ComparatorType.UTF8TYPE));
        assertThat(dataSet.getColumnFamilies().get(0).getColumnsMetadata().get(1).getIndexName(),is("indexNameOfTheIndex"));

		assertThat(dataSet.getColumnFamilies().get(0).getColumnsMetadata().get(2).getColumnName(),
				is("columnWithUTF8ValidationClass"));
		assertThat(dataSet.getColumnFamilies().get(0).getColumnsMetadata().get(2).getColumnIndexType(), nullValue());
		assertThat(dataSet.getColumnFamilies().get(0).getColumnsMetadata().get(2).getValidationClass(),
				is(ComparatorType.UTF8TYPE));
	}

	@Test
	public void shouldGetAColumnFamilyWithCompositeType() throws Exception {
		DataSet dataSet = new ClassPathXmlDataSet("xml/datasetWithCompositeType.xml");
		assertThatKeyspaceModelWithCompositeTypeIsOk(dataSet);
	}

	@Test(expected = ParseException.class)
	public void shouldNotGetAColumnFamilyWithCompositeType() throws Exception {
		DataSet dataSet = new ClassPathXmlDataSet("xml/datasetWithBadCompositeType.xml");
		dataSet.getKeyspace();
	}
}
