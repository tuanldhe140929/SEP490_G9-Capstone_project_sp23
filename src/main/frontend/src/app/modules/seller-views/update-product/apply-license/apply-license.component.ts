import { Component, Inject, OnInit } from '@angular/core';
import { MatDialog, MatDialogRef, MAT_DIALOG_DATA } from '@angular/material/dialog';
import { License } from '../../../../dtos/License';
import { ProductService } from '../../../../services/product.service';
import { ConfirmComponent } from './confirm/confirm.component';

@Component({
  selector: 'app-apply-license',
  templateUrl: './apply-license.component.html',
  styleUrls: ['./apply-license.component.css']
})
export class ApplyLicenseComponent implements OnInit {
  licenseList: License[] = [];
  productLicense: License;
  constructor(private productService: ProductService,
    @Inject(MAT_DIALOG_DATA) public data: any,
    private dialog: MatDialog,
    private dialogRef: MatDialogRef<ApplyLicenseComponent>) {

  }
  ngOnInit(): void {
    this.productService.getAllLicense().subscribe(
      (data) => {
        this.licenseList = data;
      }
    );
    this.productService.getLicenceByProductId(this.data.productId).subscribe(
      (data) => {
        this.productLicense = data;
        if (this.productLicense != null) {
          this.ApplyBtn.disabled = false;
        } else {
          this.ApplyBtn.disabled = true;
        }
      })
  }
  licenseId = 0;
  onLicenseSelect($event: any) {
    
    var licenseId = $event.target.value;
    console.log(licenseId);
    this.licenseId = licenseId;
    
    var licenseDetails = '';
    this.ApplyBtn.disabled = true;
    if (licenseId > 0) {
      this.ApplyBtn.disabled = false;
      const selectedLicense = this.licenseList.find(license => license.id === +licenseId);

      if (selectedLicense) {
        licenseDetails = `
      <h5>${selectedLicense.name}</h5>
      <p>${selectedLicense.details}</p>
      <a href="${selectedLicense.referenceLink}" target="_blank">Xem chi tiết</a>
    `;
      }
    }
    if (this.LicenseDetails != null) {
      this.LicenseDetails.innerHTML = licenseDetails;
    }
  }

  get LicenseDetails() {
    return document.getElementById('license_Details')
  }

  applyLicense() {
    const data = {
      licenseId: this.licenseId,
      productId: this.data.productId
    }
    const dialogRef = this.dialog.open(ConfirmComponent, {
      // height: '57%',
      width: '30%',
      data: data,
      panelClass: 'my-dialog-container'
    });

    dialogRef.afterClosed().subscribe(result => {
      console.log(`Dialog result: ${result}`);
      this.dialogRef.close();
      setTimeout(() => {  this.refresh(), 300 })
    });
  }
  refresh() {
    this.productService.getAllLicense().subscribe(
      (data) => {
        this.licenseList = data;
      }
    );
    this.productService.getLicenceByProductId(this.data.productId).subscribe(
      (data) => {
        this.productLicense = data;
      })
  }
  get ApplyBtn() {
    return document.getElementById('apply_btn') as HTMLButtonElement;
  }
}
