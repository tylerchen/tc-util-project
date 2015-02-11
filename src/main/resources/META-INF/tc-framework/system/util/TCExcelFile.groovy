/**
 Copyright (c) 2012, Andre Steingress
 All rights reserved.

 Redistribution and use in source and binary forms, with or without
 modification, are permitted provided that the following conditions are met:
 1. Redistributions of source code must retain the above copyright
    notice, this list of conditions and the following disclaimer.
 2. Redistributions in binary form must reproduce the above copyright
    notice, this list of conditions and the following disclaimer in the
    documentation and/or other materials provided with the distribution.
 3. All advertising materials mentioning features or use of this software
    must display the following acknowledgement:
    This product includes software developed by the ASF.
 4. Neither the name of the ASF nor the
    names of its contributors may be used to endorse or promote products
    derived from this software without specific prior written permission.

 THIS SOFTWARE IS PROVIDED BY Andre Steingress ''AS IS'' AND ANY
 EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
 WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 DISCLAIMED. IN NO EVENT SHALL Andre Steingress BE LIABLE FOR ANY
 DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
 LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.

 GSheets is a Groovy builder based on Apache POI.
 */

package org.iff.groovy.util

/**
 * A Groovy builder that wraps Apache POI for generating binary Microsoft Excel sheets.
 *
 * <pre>
 *
 * Workbook workbook = new ExcelBuilder().workbook {
 *
 * // define cell styles and fonts
 * styles {
 *   font("bold")  { Font font ->
 *       font.setBoldweight(Font.BOLDWEIGHT_BOLD)
 *   }
 *
 *   cellStyle ("header")  { CellStyle cellStyle ->
 *       cellStyle.setAlignment(CellStyle.ALIGN_CENTER)
 *   }
 * }
 *
 * // declare the data to use
 * data {
 *   sheet ("Export")  {
 *       header(["Column1", "Column2", "Column3"])
 *
 *       row(["a", "b", "c"])
 *   }
 * }
 *
 * // apply link styles with data through 'commands'
 * commands {
 *     applyCellStyle(cellStyle: "header", font: "bold", rows: 1, columns: 1..3)
 *     mergeCells(rows: 1, columns: 1..3)
 * }
 * }
 *
 * </pre>
 *
 * @author me@andresteingress.com
 */
class ExcelBuilder {

    def workbook = org.iff.infra.util.ReflectHelper.getConstructor('org.apache.poi.hssf.usermodel.HSSFWorkbook').newInstance()
    def sheet
    def int rowsCounter

    def cellStyles = [:]
    def fonts = [:]

    /**
     * Creates a new workbook.
     *
     * @param the closure holds nested {@link ExcelBuilder} method calls
     * @return the created {@link Workbook}
     */
    def workbook(closure) {
        assert closure
        closure.delegate = this
        closure.call()
        workbook
    }
	
	def styles(closure) {
		assert closure
		closure.delegate = this
		closure.call()
	}
	
	def data(closure) {
		assert closure
		closure.delegate = this
		closure.call()
	}
	
	def commands(closure) {
		assert closure
		closure.delegate = this
		closure.call()
	}

    def sheet(name, closure) {
        assert workbook
        assert name
        assert closure

        sheet = workbook.createSheet(name)
        rowsCounter = 0
        closure.delegate = sheet
        closure.call()
    }

    def cellStyle(cellStyleId, closure)  {
        assert workbook
        assert cellStyleId
        assert !cellStyles.containsKey(cellStyleId)
        assert closure

        def cellStyle = workbook.createCellStyle()
        cellStyles.put(cellStyleId, cellStyle)
        closure.call(cellStyle)
    }

    def font(fontId, closure)  {
        assert workbook
        assert fontId
        assert !fonts.containsKey(fontId)
        assert closure

        def font = workbook.createFont()
        fonts.put(fontId, font)
        closure.call(font)
    }

    def applyCellStyle(map)  {
        assert workbook

        def cellStyleId = map.cellStyle
        def fontId = map.font
        def dataFormat = map.dataFormat
        def sheetName = map.sheet
        def rows = map.rows ?: -1          // -1 denotes all rows
        def cells = map.columns ?: -1      // -1 denotes all cols
        def colName = map.columnName

        assert cellStyleId || fontId || dataFormat
        assert rows && (rows in Number || rows instanceof Range<Number>)
        assert cells && (cells in Number || cells instanceof Range<Number>)
		
		cellStyleId = (cellStyleId && !cellStyles.containsKey(cellStyleId)) ? null : cellStyleId
		fontId = (fontId && !fonts.containsKey(fontId)) ? null : fontId
		dataFormat = (dataFormat && !(dataFormat instanceof String)) ? null : dataFormat
		sheetName = (sheetName && !(sheetName instanceof String)) ? null : sheetName
		colName = (colName && !(colName instanceof String)) ? null : colName

        def sheet = sheetName ? workbook.getSheet(sheetName as String) : workbook.getSheetAt(0)
        assert sheet

		rows = rows == -1 ? [1..rowsCounter] : rows
		rows = rows in  Number ? [rows] : rows

        rows.each { Number rowIndex ->
            assert rowIndex

            def row = sheet.getRow(rowIndex.intValue() - 1)
            if (!row){
				return
			}

            cells  = (cells == -1)  ? [row.firstCellNum..row.lastCellNum] : cells
            rows = (rows in  Number) ? [rows] : rows

            def applyStyleFunc = { cellIndex ->
                assert cellIndex

                def cell = row.getCell(cellIndex.intValue() - 1)
                if (!cell){
					return
				}

                if (cellStyleId) {
					cell.setCellStyle(cellStyles.get(cellStyleId))
                }
                if (fontId) {
					cell.getCellStyle().setFont(fonts.get(fontId))
                }
                if (dataFormat) {
                    def df = workbook.createDataFormat()
                    cell.getCellStyle().setDataFormat(df.getFormat(dataFormat as String))
                }
            }

            cells.each applyStyleFunc
        }
    }

    def mergeCells(map)  {
        assert workbook

        def rows = map.rows
        def cols = map.columns
        def sheetName = map.sheet

        assert rows && (rows in Number || rows instanceof Range<Number>)
        assert cols && (cols in Number || cols instanceof Range<Number>)

        rows = (rows in Number) ? [rows] : rows
        cols = (cols in Number) ? [cols] : cols
        sheetName = (sheetName && !(sheetName in String)) ? null : sheetName

        def sheet = sheetName ? workbook.getSheet(sheetName as String) : workbook.getSheetAt(0)
		def cellRangeAddress=org.iff.infra.util.ReflectHelper.getConstructor('org.apache.poi.ss.util.CellRangeAddress','int','int','int','int')
        sheet.addMergedRegion(cellRangeAddress.newInstance((rows.first() - 1) as int, (rows.last() - 1) as int, (cols.first() - 1) as int, (cols.last() - 1) as int))
    }

    def applyColumnWidth(map)  {
        assert workbook

        def cols = map.columns
        def sheetName = map.sheet
        def width = map.width

        assert cols && (cols in Number || cols instanceof Range<Number>)
        assert width && width in Number

        cols = (cols in Number) ? [cols] : cols
        sheetName = (sheetName && !(sheetName in String)) ? null : sheetName

        def sheet = sheetName ? workbook.getSheet(sheetName as String) : workbook.getSheetAt(0)

        cols.each {
            sheet.setColumnWidth(it - 1, width.intValue())
        }
    }

    def header(names)  {
        assert sheet
        assert names

        def row = sheet.createRow(rowsCounter++ as int)
        names.eachWithIndex { value, col ->
            def cell = row.createCell(col)
            cell.setCellValue(value)
        }
    }

    def emptyRow()  {
        assert sheet
        sheet.createRow(rowsCounter++ as int)
    }

    def row(values) {
        assert sheet
        assert values

        def row = sheet.createRow(rowsCounter++ as int)
        values.eachWithIndex {value, col ->
            def cell = row.createCell(col)
            switch (value) {
                case Date: cell.setCellValue((Date) value); break
                case Double: cell.setCellValue((Double) value); break
                case BigDecimal: cell.setCellValue(((BigDecimal) value).doubleValue()); break
                case Number: cell.setCellValue(((Number) value).doubleValue()); break
                default:
                    def stringValue = value?.toString() ?: ""
                    if (stringValue.startsWith('=')) {
                        cell.setCellType(cell.CELL_TYPE_FORMULA)
                        cell.setCellFormula(stringValue.substring(1))
                    } else {
						def hssfRichTextString=org.iff.infra.util.ReflectHelper.getConstructor('org.apache.poi.hssf.usermodel.HSSFRichTextString', 'java.lang.String')
                        cell.setCellValue(hssfRichTextString.newInstance(stringValue.toString()))
                    }
                    break
            }
        }
    }

    def getRowCount()  {
        assert sheet
        rowsCounter
    }
}
