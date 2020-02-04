@echo off
rem This batch is to reverse the cancel transactions

set method=%1
set filepath=%2

java -jar  %CD%\TaqseetReconciliationBatch.jar "%method%" "%filepath%"