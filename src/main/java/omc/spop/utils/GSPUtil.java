package omc.spop.utils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.Stack;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import gudusoft.gsqlparser.EDbObjectType;
import gudusoft.gsqlparser.ETableSource;
import gudusoft.gsqlparser.TCustomSqlStatement;
import gudusoft.gsqlparser.TGSqlParser;
import gudusoft.gsqlparser.TSourceToken;
import gudusoft.gsqlparser.nodes.TColumnWithSortOrder;
import gudusoft.gsqlparser.nodes.TObjectName;
import gudusoft.gsqlparser.nodes.TObjectNameList;
import gudusoft.gsqlparser.nodes.TTable;
import gudusoft.gsqlparser.nodes.TTypeName;
import gudusoft.gsqlparser.stmt.TStoredProcedureSqlStatement;


/***********************************************************
 * 2020.08.26	이재우	최초작성.
 * reference : gudusoft.gsqlparser.trial.2.0.6.5\gudusoft.gsqlparser.trial.2.0.6.5\src\main\java\demos\gettablecolumns\TGetTableColumn.java를 참조하였음.
 **********************************************************/
public class GSPUtil {
	private static final Logger logger 						= LoggerFactory.getLogger(GSPUtil.class);
	private static final String NOT_APP 					= "N/A";
	public static final String SELFSQL_STD_QTY_TAB_COLS		= "SELFSQL_STD_QTY_TAB_COLS";
	public static final String SELFSQL_STD_QTY_TABLES 		= "SELFSQL_STD_QTY_TABLES";
	public static final String GSP 							= "GSPUtil";
	
	private static class TInfoRecord {
		private TObjectName index;
		private EDbObjectType dbObjectType;
		private String fileName = NOT_APP;
		private TObjectName SPName; //stored procedure name
		private TTable table;
		private TObjectName column;
		
		public void setIndex(TObjectName index) {
			this.index = index;
		}
		
		public TObjectName getIndex() {
			return index;
		}
		
		public EDbObjectType getDbObjectType() {
			return dbObjectType;
		}
		
		public void setDbObjectType(EDbObjectType dbObjectType) {
			this.dbObjectType = dbObjectType;
		}
		
		public TInfoRecord(EDbObjectType dbObjectType) {
			this.dbObjectType = dbObjectType;
		}
		
		public TInfoRecord(TTable table) {
			this.table = table;
			this.dbObjectType = EDbObjectType.table;
		}
		
		public TInfoRecord(TInfoRecord clone, EDbObjectType dbObjectType) {
			this.fileName = clone.fileName;
			this.SPName = clone.SPName;
			this.table = clone.table;
			this.column = clone.column;
			this.dbObjectType = dbObjectType;
		}
		
		public void setSPName(TObjectName SPName) {
			this.SPName = SPName;
		}
		
		public TObjectName getSPName() {
			return SPName;
		}
		
		public void setFileName(String fileName) {
			this.fileName = fileName;
		}
		
		public String getFileName() {
			return fileName;
		}
		
		public void setTable(TTable table) {
			this.table = table;
		}
		
		public TTable getTable() {
			return table;
		}
		
		public void setColumn(TObjectName column) {
			this.column = column;
		}
		
		public TObjectName getColumn() {
			return column;
		}
		
		public String getSPString() {
			TObjectName sp_name = getSPName();
			
			if ( sp_name == null ) {
				return NOT_APP;
			} else {
				return sp_name.toString();
			}
		}
		
		public String getTableStr(TTable table) throws Exception {
			StringBuffer tableName = new StringBuffer();
			
			try {
				if ( table.getTableType() == ETableSource.subquery ) {
					tableName.append("(subquery, alias:").append( table.getAliasName() ).append(RIGHT_BRACKET);
				} else {
					tableName.append( table.getTableName().toString() );
					
					if ( table.isLinkTable() ) {
						tableName.append(LEFT_BRACKET).append(table.getLinkTable().getTableName().toString()).append(RIGHT_BRACKET);
					} else if ( table.isCTEName() ) {
						tableName.append(tableName).append("(CTE)");
					}
				}
			} catch (Exception e) {
				String methodName = new Object() {}.getClass().getEnclosingMethod().getName();
				logger.error( "[ " + GSP + "." + methodName + ".getTableStr ] " + e.getMessage() );
				e.printStackTrace();
				throw e;
			}
			
			return tableName.toString();
		}
		
		public String printMe(boolean includingTitle) throws Exception {
			final String SP_TITLE 		= "\nfilename|spname|object type\n";
			final String TABLE_TITLE 	= "\nfilename|spname|object type|schema|table|table effect\n";
			final String INDEX_TITLE 	= "\nfilename|spname|object type|schema|index|table|column|location|coordinate\n";
			final String COLUMN_TITLE 	= "\nfilename|spname|object type|schema|table|column|location|coordinate|datatype\n";
			
			String schemaName = NOT_APP;
			String tableName = "unknownTable";
			String indexName = "unknownIndex";
			
			TTable ttable = getTable();
			TObjectName col = getColumn();
			TObjectName index = getIndex();
			String fileName = getFileName();
			String spString = getSPString();
			
			StringBuffer analyzeCol = new StringBuffer(1024);
			
			try {
				switch ( dbObjectType ) {
					case procedure:
						if ( includingTitle ) {
							analyzeCol.append(SP_TITLE);
						}
						
						analyzeCol.append( fileName ).append( "|" ).append( getSPName().toString() ).append( "|" ).append( dbObjectType );
						
						break;
					case table:
						if ( includingTitle ) {
							analyzeCol.append( TABLE_TITLE );
						}
						
						tableName = getTableStr( ttable );
						schemaName = ttable.getPrefixSchema();
						
						if ( schemaName.length() == 0 ) {
							schemaName = NOT_APP;
						}
						
						analyzeCol.append( fileName ).append( "|" ).append( spString ).append( "|" ).append( dbObjectType ).append( "|" );
						analyzeCol.append( schemaName ).append( "|" ).append( tableName ).append( "|" ).append( ttable.getEffectType() );
						
						break;
					case column:
						if ( includingTitle ) {
							analyzeCol.append( COLUMN_TITLE );
						}
						
						if ( ttable != null ) {
							//it's an orphan column
							tableName = getTableStr( ttable );
							schemaName = ttable.getPrefixSchema();
							
							if ( schemaName.length() == 0 ) {
								schemaName = NOT_APP;
							}
						} else {
							tableName = "missed";
						}
						
						StringBuffer datatypeStr = new StringBuffer();
						
						if ( col.getLinkedColumnDef() != null ) {
							//column in create table, add datatype information as well
							TTypeName datatype = col.getLinkedColumnDef().getDatatype();
							datatypeStr.append(datatype.getDataTypeName());
							
							if ( datatype.getLength() != null ) {
								datatypeStr.append( datatypeStr ).append( ":" ).append( datatype.getLength().toString() );
							} else if ( datatype.getPrecision() != null) {
								datatypeStr.append( datatypeStr ).append( ":" ).append( datatype.getPrecision().toString() );
								
								if ( datatype.getScale() != null ) {
									datatypeStr.append( datatypeStr ).append( ":" ).append( datatype.getScale().toString() );
								}
							}
						}
						analyzeCol.append( fileName ).append( "|" ).append( spString ).append( "|" ).append( dbObjectType ).append( "|" ).append( schemaName ).append( "|" ).append( tableName ).append( "|" );
						analyzeCol.append( col.toString() ).append( "|" ).append( col.getLocation() ).append( "|(" ).append( col.coordinate() ).append( ")|" ).append( datatypeStr ).append( " | " );
						
						try {
							analyzeCol.append( ttable.getAliasName() );
						} catch ( NullPointerException e ) {
							analyzeCol.append( NOT_APP );
						}
						
						break;
					case index:
						if ( includingTitle ) {
							analyzeCol.append( INDEX_TITLE );
						}
						
						if ( ttable != null ) {
							schemaName = ttable.getPrefixSchema();
							
							if ( schemaName.length() == 0 ) {
								schemaName = NOT_APP;
							}
							
							tableName = ttable.getTableName().toString();
						}
						
						if ( index != null ) {
							indexName = index.toString();
						}
						
						analyzeCol.append( fileName ).append( "|" ).append( spString ).append( "|" ).append( dbObjectType ).append( "|" ).append( schemaName ).append( "|" );
						analyzeCol.append(indexName).append("|").append( tableName ).append( "|" ).append( col.toString() ).append( "|" ).append( col.getLocation() );
						analyzeCol.append( "|(" ).append( col.coordinate() ).append( RIGHT_BRACKET ).append( " | " );
						
						break;
					default: 
						analyzeCol.append("");
						
						break;
				}
			} catch ( Exception e ) {
				String methodName = new Object() {}.getClass().getEnclosingMethod().getName();
				logger.error( "[ " + GSP + "." + methodName + ".printMe ] " + e.getMessage() );
				e.printStackTrace();
				throw e;
			}
			
			return analyzeCol.toString();
		}
	}
	
	private static ArrayList<TInfoRecord> infoList;
	private static ArrayList<String> fieldlist;
	private static Stack<TStoredProcedureSqlStatement> spList;
	
	private static StringBuffer infos;
	
	private static final String NEW_LINE 		= "\n";
	private static final String DOT_CHAR 		= ".";
	private static final String LEFT_BRACKET 	= "(";
	private static final String RIGHT_BRACKET 	= ")";
	
	private static boolean linkOrphanColumnToFirstTable = true;
	private static boolean listStarColumn = false;
	private static boolean showTableEffect = false;
	private static boolean showColumnLocation = false;
	private static boolean showDatatype = false;
	
	protected static String numberOfSpace( int pNum ) throws Exception {
		StringBuffer ret = new StringBuffer();
		
		try {
			for ( int num = 0; num < pNum; num++ ) {
				ret.append( ret ).append( " " );
			}
		} catch ( Exception e ) {
			String methodName = new Object() {}.getClass().getEnclosingMethod().getName();
			logger.error( "[" + GSP + "." + methodName + ".numberOfSpace ] " + e.getMessage() );
			e.printStackTrace();
			throw e;
		}
		
		return ret.toString();
	}
	
	protected static class SortIgnoreCase implements Comparator<Object> {
		public int compare( Object o1, Object o2 ) {
			String s1 = (String) o1;
			String s2 = (String) o2;
			
			return s1.toLowerCase().compareTo(s2.toLowerCase());
		}
	}
	
	protected static void removeDuplicateAndSort( ArrayList <String> pList ) throws Exception {
		Collections.sort( pList, new SortIgnoreCase() );
		
		try {
			for ( int target = 0 ; target < pList.size() - 1 ; target ++ ) {
				for ( int compare = pList.size() - 1 ; compare > target; compare -- ) {
					
					if ( pList.get(compare).equalsIgnoreCase( pList.get(target) ) ) {
						pList.remove(compare);
					}
				}
			}
		} catch ( Exception e ) {
			String methodName = new Object() {}.getClass().getEnclosingMethod().getName();
			logger.error( "[ " + GSP + "." + methodName + ".removeDuplicateAndSort ] " + e.getMessage() );
			e.printStackTrace();
			throw e;
		}
	}
	
	protected static ArrayList<HashMap<String, Object>> setShowSummary( ) throws Exception {
		ArrayList<HashMap<String, Object>> setCols = new ArrayList<HashMap<String, Object>>(); 
		HashMap<String, Object> setCol;
		
		for ( String string : fieldlist ) {
			setCol = new HashMap<String, Object>();
			String[] tabcol = string.replaceAll("\"", "").split("[.]");
			
			setCol.put( "sql_std_original_div_cd", 1 );
			
			// sql_std_original_div_cd = Classification code
			if ( tabcol.length > 2 ) {
				setCol.put( "object_owner"	, tabcol[0] );
				setCol.put( "table_name"		, tabcol[1] );
				setCol.put( "column_name"	, tabcol[2] );
			} else {
				setCol.put( "object_owner"	, NOT_APP );
				setCol.put( "table_name"		, tabcol[0] );
				setCol.put( "column_name"	, tabcol[1] );
			}
			
			setCol.put( "sp_name"			, NOT_APP );
			setCol.put( "table_alias"		, NOT_APP );
			setCol.put( "column_location"	, NOT_APP );
			setCol.put( "column_coordinate"	, NOT_APP );
			
			setCols.add( setCol );
		}
		
		return setCols;
	}
	
	protected static HashMap<String, Object> setShowDetail( String[] str ) throws Exception {
		HashMap<String, Object> setDetail = new HashMap<String, Object>();
		
		// sql_std_original_div_cd = Classification code
		setDetail.put( "sql_std_original_div_cd", 2 );
		setDetail.put( "sp_name", str[1] );
		setDetail.put( "object_owner", str[3] );
		setDetail.put( "table_name", str[4] );
		
		if ( str[9].trim().equals("") ) {
			setDetail.put( "table_alias", NOT_APP );
		} else {
			setDetail.put( "table_alias", str[9].trim() );
		}
		
		setDetail.put( "column_name", str[5] );
		setDetail.put( "column_location", str[6] );
		setDetail.put( "column_coordinate", str[7] );
		
		return setDetail;
	}
	
	public static Map<String, Object> getTableColumn( String sql ) throws RuntimeException, Exception {
		ArrayList<HashMap<String, Object>> getColDetail = new ArrayList<HashMap<String,Object>>();
		ArrayList<HashMap<String, Object>> getTable = new ArrayList<HashMap<String,Object>>();
		
		Map<String, Object> selfSqlTableCol = new HashMap<String, Object>();
		HashMap<String, Object> getTabCol; 
		
		TGSqlParser sqlParser = new TGSqlParser(TGSqlParser.getDBVendorByName("oracle"));
		
		String[] str;
		
		boolean includingTitle = true;
		String oneLine = "";
		
//		linkOrphanColumnToFirstTable = false;
		showDatatype = true;
		listStarColumn = true;
		showColumnLocation = true;
		
		infoList = new ArrayList<TInfoRecord>();
		fieldlist = new ArrayList<String>();
		infos = new StringBuffer();
		
		sqlParser.sqltext = sql;
		
		if ( sqlParser.parse() != 0 ) {
			String methodName = new Object() {}.getClass().getEnclosingMethod().getName();
			throw new RuntimeException("[" + GSP + "." + methodName +" ], "+sqlParser.getErrormessage());
		}
		
		fieldlist.clear();
		
		for ( int sqlIdx = 0; sqlIdx < sqlParser.sqlstatements.size(); sqlIdx++ ) {
			analyzeStmt(sqlParser.sqlstatements.get(sqlIdx),0);
		}
		
		// column/showSummary
		removeDuplicateAndSort( fieldlist );
		
		getColDetail.addAll( setShowSummary() );
		
		try {
			for ( int infoIdx = 0; infoIdx < infoList.size(); infoIdx++ ) {
				if ( infoIdx > 0 ) {
					includingTitle = infoList.get(infoIdx).getDbObjectType() != infoList.get(infoIdx-1).getDbObjectType();
				}
				
				oneLine = infoList.get( infoIdx ).printMe( includingTitle );
				
				// Extract only the required values from the line with "\n"
				if ( oneLine.contains("\n") ) {
					String[] strOne = oneLine.split( "\\\n" );
					oneLine = strOne[2];
				}
				
				str = oneLine.split("\\|");
				
				if ( oneLine.contains("|tet") ) {
					boolean tableChk = false;
					
					for ( int tableIdx = 0; tableIdx < getTable.size(); tableIdx++ ) {
						tableChk = getTable.get( tableIdx ).get( "table_name" ).equals( str[4] );
					}
					
					// Table
					if ( !oneLine.contains( "alias:" ) && !oneLine.contains("(CTE)") && !tableChk ) {
						getTabCol = new HashMap<String, Object>();
						
						if ( str[3].contains( "\"" ) ) {
							str[3] = str[3].replaceAll("\"", "").trim();
							str[4] = str[4].split("[.]")[1].replaceAll("\"", "").trim();
						}
						
						if (str[4].contains(".")) {
							str[4] = str[4].split("[.]")[1].trim();
						}
						
						getTabCol.put( "object_owner", str[3] );
						getTabCol.put( "table_name", str[4] );
						getTabCol.put( "table_effect", str[str.length-1] );
						
						getTable.add( getTabCol );
					} else {
						continue;
					}
				} else {
					// showDetail
					getColDetail.add( setShowDetail( str ) );
				}
			}
		} catch ( Exception e ) {
			String methodName = new Object() {}.getClass().getEnclosingMethod().getName();
			logger.error( "[ " + GSP + "." + methodName + " ] " + e.getMessage() );
			e.printStackTrace();
			throw e;
		}
		
		selfSqlTableCol.put(SELFSQL_STD_QTY_TABLES, getTable);
		selfSqlTableCol.put(SELFSQL_STD_QTY_TAB_COLS, getColDetail);
		
		return selfSqlTableCol;
	}
	
	protected static void analyzeStmt( TCustomSqlStatement stmt, int pNest ) throws Exception {
		spList = new Stack<TStoredProcedureSqlStatement>();
		
		try {
			getInfoTableCol( spList, stmt, pNest );
			getInfoOrphanColumn( stmt, pNest );
			getSubQueryColRecord( spList, stmt, pNest );
		} catch ( Exception e ) {
			String methodName = new Object() {}.getClass().getEnclosingMethod().getName();
			logger.error( "[ " + GSP + "." + methodName + ".analyzeStmt ]" + e.getMessage() );
			e.printStackTrace();
			throw e;
		}
	}
	
	protected static void getInfoTableCol( Stack<TStoredProcedureSqlStatement> spList, TCustomSqlStatement stmt, int pNest ) throws Exception {
		TInfoRecord spRecord;
		TInfoRecord tableRecord;
		TInfoRecord columnRecord;
		int stmtSize; 
		
		TTable lcTable = null;
		TObjectName lcColumn = null;
		StringBuffer columnStr= new StringBuffer();
		StringBuffer tableStr = new StringBuffer();
		StringBuffer locationStr = new StringBuffer();
		String sqlFileName = NOT_APP;
		
		TObjectNameList LinkedCols;
		
		try {
			if ( stmt instanceof TStoredProcedureSqlStatement ) {
				spList.push( (TStoredProcedureSqlStatement) stmt );
				
				spRecord = new TInfoRecord( EDbObjectType.procedure );
				spRecord.setSPName( spList.peek().getStoredProcedureName() );
			}
			
			// sqlstatementtype = effect type 
			infos.append( numberOfSpace( pNest ) ).append( stmt.sqlstatementtype ).append(NEW_LINE);
			
			for ( int tableIdx = 0; tableIdx < stmt.tables.size(); tableIdx++ ) {
				lcTable = stmt.tables.getTable( tableIdx );
				LinkedCols = lcTable.getLinkedColumns();
				stmtSize = LinkedCols.size();
				tableRecord = new TInfoRecord( lcTable );
				tableStr.setLength(0);
				
				tableRecord.setFileName( sqlFileName );
				
				if (spList.size() > 0) {
					tableRecord.setSPName( spList.peek().getStoredProcedureName() );
				}
				
				// infoList add
				infoList.add( tableRecord );
				
				// Identify the alias in SubQuery.
				if ( lcTable.getTableType( ) == ETableSource.subquery ) {
					tableStr.append("(subquery, alias:").append(lcTable.getAliasName( )).append(RIGHT_BRACKET);
				} else if ( lcTable.getTableType( ) == ETableSource.tableExpr ) {
					tableStr.append("(table expression, alias:").append( lcTable.getAliasName( ) ).append(RIGHT_BRACKET);
				} else if ( lcTable.getTableType( ) == ETableSource.openquery ) {
					tableStr.append("(table openquery, alias:").append( lcTable.getAliasName( ) ).append(RIGHT_BRACKET);
					
					analyzeStmt( lcTable.getSubquery() , pNest++ );
				} else if ( lcTable.getTableType() == ETableSource.function ) {
					tableStr.append( "(table-valued function:" ).append( lcTable.getTableName( ) ).append(RIGHT_BRACKET);
				} else if ( lcTable.getTableName( ) != null ) {
					tableStr.append( lcTable.getTableName( ).toString( ) );
					
					if ( lcTable.isLinkTable( ) ) {
						tableStr.append(tableStr).append(LEFT_BRACKET).append( lcTable.getLinkTable( ).getTableName( ).toString( ) ).append(RIGHT_BRACKET);
					} else if ( lcTable.isCTEName( ) ) {
						tableStr.append(tableStr).append("(CTE)");
					}
				}
				
				// Insert AliasName and column in SubQuery into infos.
				if ( (showTableEffect) && (lcTable.isBaseTable()) ) {
					infos.append( numberOfSpace( pNest + 1 ) ).append( tableStr ).append(LEFT_BRACKET).append( lcTable.getEffectType() ).append(RIGHT_BRACKET).append(NEW_LINE);
				} else {
					infos.append( numberOfSpace( pNest + 1 ) ).append( tableStr ).append(NEW_LINE);
				}
				
				// judgment of column
				for ( int colIdx = 0; colIdx < stmtSize; colIdx++ ) {
					lcColumn = LinkedCols.getObjectName( colIdx );
					
					columnRecord = new TInfoRecord( tableRecord ,EDbObjectType.column);
					
					columnRecord.setColumn( lcColumn );
					infoList.add( columnRecord );
					
					columnStr.setLength(0);
					
					columnStr.append(lcColumn.getColumnNameOnly());
					
					if ( (showDatatype) && (lcColumn.getLinkedColumnDef() != null) ) {
						//column in create table, add datatype information as well
						TTypeName datatype = lcColumn.getLinkedColumnDef().getDatatype();
						columnStr.append( columnStr ).append( ":" ).append( datatype.getDataTypeName() );
						
						if ( datatype.getLength() != null ) {
							columnStr.append( columnStr ).append( ":" ).append( datatype.getLength().toString() );
						} else if ( datatype.getPrecision() != null ) {
							columnStr.append( columnStr ).append( ":" ).append( datatype.getPrecision().toString() );
							
							if ( datatype.getScale() != null ) {
								columnStr.append( columnStr ).append( ":" ).append( datatype.getScale().toString() );
							}
						}
					}
					
					// Check the location of the column and insert it into the infos.(where,select)
					locationStr.setLength(0);
					
					if ( showColumnLocation ) {
						if ( lcColumn.getColumnToken() != null ) {
							TSourceToken lcStartToken = lcColumn.getColumnToken();
							locationStr.append(LEFT_BRACKET).append( lcStartToken.lineNo ).append( "," ).append( lcStartToken.columnNo ).append(RIGHT_BRACKET);
						}
						
						infos.append( numberOfSpace( pNest + 3 ) ).append( lcColumn.getColumnNameOnly() ).append( locationStr ).append(LEFT_BRACKET).append( lcColumn.getLocation() ).append(RIGHT_BRACKET).append(NEW_LINE);
					} else {
						infos.append( numberOfSpace( pNest + 3 ) ).append( lcColumn.getColumnNameOnly() ).append(NEW_LINE);
					}
					
					// Determine if it is a subquery, determine if * is in columnName, and insert it into table name.column name filedlist. 
					if ( !( ( lcTable.getTableType() == ETableSource.subquery ) || ( lcTable.isCTEName() ) ) ) {
						if ( (listStarColumn) || ( !( lcColumn.getColumnNameOnly().equals("*") ) ) ) {
							
							if ( lcTable.isLinkTable() ) {
								fieldlist.add( lcTable.getLinkTable().getTableName().toString() + DOT_CHAR + columnStr );
							} else {
								fieldlist.add( tableStr + DOT_CHAR + columnStr );
							}
						}
					}
				}
			}
		} catch ( Exception e ) {
			String methodName = new Object() {}.getClass().getEnclosingMethod().getName();
			logger.error( "[ " + GSP + "." + methodName + ".getInfoTableCol ] " + e.getMessage() );
			e.printStackTrace();
			throw e;
		}
	}
	
	protected static void getInfoOrphanColumn( TCustomSqlStatement stmt, int pNest ) throws Exception {
		String sqlFileName = NOT_APP;
		TObjectNameList stmtOrphanColList = stmt.getOrphanColumns();
		TInfoRecord columnRecord;
		
		TObjectName stmtOrphanColName;
		TTable stmtPhysicalTable;
		
		try {
			if ( stmtOrphanColList.size() > 0 ) {
				String orphanCol = "";
				infos.append( numberOfSpace( pNest + 1 ) ).append( " orphan columns:" ).append(NEW_LINE);
				
				for ( int orphanColIdx = 0; orphanColIdx < stmtOrphanColList.size(); orphanColIdx++ ) {
					columnRecord = new TInfoRecord( EDbObjectType.column );
					stmtOrphanColName = stmtOrphanColList.getObjectName( orphanColIdx );
					
					columnRecord.setFileName( sqlFileName );
					columnRecord.setColumn( stmtOrphanColName );
					infoList.add( columnRecord );
					
					orphanCol = stmtOrphanColName.getColumnNameOnly();// stmt.getOrphanColumns().getObjectName(k).toString();
					stmtPhysicalTable = stmt.getFirstPhysicalTable();
					
					if ( showColumnLocation ) {
						infos.append( numberOfSpace( pNest + 3 ) ).append( orphanCol ).append(LEFT_BRACKET).append( stmtOrphanColName.getLocation() ).append(RIGHT_BRACKET).append(NEW_LINE);
					} else {
						infos.append( numberOfSpace( pNest + 3 ) ).append( orphanCol ).append(NEW_LINE);
					}
					
					if ( (linkOrphanColumnToFirstTable) && (stmtPhysicalTable != null) ) {
						if ( ( listStarColumn ) || ( !( orphanCol.equalsIgnoreCase("*") ) ) ) {
							fieldlist.add( stmtPhysicalTable.toString() + DOT_CHAR + orphanCol );
						}
						columnRecord.setTable( stmtPhysicalTable );
						
					} else {
						fieldlist.add( "missed" + DOT_CHAR + orphanCol + LEFT_BRACKET + stmtOrphanColName.coordinate() + RIGHT_BRACKET );
					}
				}
			}
		} catch (Exception e) {
			String methodName = new Object() {}.getClass().getEnclosingMethod().getName();
			logger.error( "[ " + GSP + "." + methodName + ".getInfoOrphanColumn ] " + e.getMessage() );
			e.printStackTrace();
			throw e;
		}
	}
	
	protected static void getSubQueryColRecord( Stack<TStoredProcedureSqlStatement> spList, TCustomSqlStatement stmt, int pNest ) throws Exception {
		TInfoRecord indexRecord;
		TColumnWithSortOrder indexColumn;
		
		try {
			for ( int colIdx = 0; colIdx < stmt.getIndexColumns().size(); colIdx++ ) {
				indexColumn = stmt.getIndexColumns().getElement( colIdx );
				indexRecord = new TInfoRecord( EDbObjectType.index );
				
				indexRecord.setColumn( indexColumn.getColumnName() );
				
				infoList.add( indexRecord );
				
				if ( indexColumn.getOwnerTable() != null ) {
					indexRecord.setTable( indexColumn.getOwnerTable() );
				}
				
				if ( indexColumn.getOwnerConstraint() != null && indexColumn.getOwnerConstraint().getConstraintName() != null ) {
					indexRecord.setIndex( indexColumn.getOwnerConstraint().getConstraintName() );
				}
			}
			
			for ( int sqlIdx = 0; sqlIdx < stmt.getStatements().size(); sqlIdx++ ) {
				analyzeStmt( stmt.getStatements().get( sqlIdx ) , pNest + 1 );
			}
			
			if ( stmt instanceof  TStoredProcedureSqlStatement ) {
				TStoredProcedureSqlStatement p = (TStoredProcedureSqlStatement) stmt;
				
				for ( int bodySqlIdx = 0; bodySqlIdx < p.getBodyStatements().size(); bodySqlIdx++) {
					analyzeStmt( p.getBodyStatements().get( bodySqlIdx ) , pNest + 1 );
				}
				
				spList.pop();
			}
		} catch ( Exception e ) {
			String methodName = new Object() {}.getClass().getEnclosingMethod().getName();
			logger.error( "[ " + GSP + "." + methodName + ".getSubQueryColRecord ] " + e.getMessage() );
			e.printStackTrace();
			throw e;
		}
	}
}
