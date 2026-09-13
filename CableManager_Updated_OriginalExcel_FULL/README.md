# Cable Manager - Excel Import Edition

This is a complete Android project for importing the original cable customer Excel report from the app frontend.

## Main features
- Import Excel from the phone using **Import Excel**.
- Supports legacy `.xls` files and modern `.xlsx` files.
- Detects the `BOX ID` column and uses BOX ID as the unique customer key.
- Adds new BOX IDs and updates existing BOX IDs.
- Reports Added / Updated / Skipped / Errors after every import.
- Stores imported customer data locally on the phone.
- Search by BOX ID, customer name, phone, address or search code.
- Dashboard calculates Paid amount, Due amount, Pending customer count and Total Outstanding.
- Next month's bill is shown from the package cost.

## Original Excel
The supplied original report is included at:
`app/src/main/assets/Box Report1788929507591.xlsx`

Note: the supplied file has an `.xlsx` filename but its original binary format is legacy Microsoft Excel (BIFF/XLS). The app uses Apache POI's `WorkbookFactory`, so the file can still be opened and imported.

## Build without Android Studio
GitHub Actions is configured in:
`.github/workflows/build-apk.yml`

After pushing this project to GitHub:
1. Open **Actions**.
2. Select **Build APK**.
3. Run the workflow (or push a commit; it also runs automatically).
4. Open the completed run.
5. Download the **CableManager-debug-apk** artifact.

## Frontend import flow
Open the app -> **Import Excel** -> choose the Excel file from the phone -> import completes -> customer list and dashboard refresh automatically.
