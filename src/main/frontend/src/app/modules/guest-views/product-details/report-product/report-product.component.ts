import { Component, Inject, OnInit } from '@angular/core';
import { FormBuilder, Validators } from '@angular/forms';
import { MAT_DIALOG_DATA } from '@angular/material/dialog';
import { ViolationType } from 'src/app/DTOS/ViolationType';
import { ReportService } from 'src/app/services/report.service';
import { ViolationTypeService } from 'src/app/services/violation-type.service';


@Component({
  selector: 'app-report-product',
  templateUrl: './report-product.component.html',
  styleUrls: ['./report-product.component.css']
})
export class ReportProductComponent implements OnInit{
  dialog: any;
  showMessage = false;
  constructor(
    private vioTypeService: ViolationTypeService,
    private reportService: ReportService,
    private formBuilder: FormBuilder,
    @Inject(MAT_DIALOG_DATA) public data: any){}

    description: string;
    violationId: number;

  vioTypeList: ViolationType[] = [];

  ngOnInit(): void {
    this.getAllVioTypes();
  }

  addReportForm = this.formBuilder.group({
    productId:[this.data.productId],
    userId:[this.data.userId],
    violationtype: [new ViolationType,[Validators.required]],
    description: ['',[Validators.required]]
  })

  getAllVioTypes(){
    this.vioTypeService.getAllTypes().subscribe(
      response => {
        this.vioTypeList = response;
      }
    )
  }


  addReport(){
    console.log(this.addReportForm.value);

    if(this.addReportForm.valid){
      this.reportService.addReport(this.addReportForm.value).subscribe(
        data => {console.log(data)
        },error=>{console.log(error)}
        
      )
    }
  }
  openSuccess(){
    this.showMessage = true;

    // Hide the message after a few seconds
    setTimeout(() => {
      this.showMessage = false;
    }, 1500);
  }
 
}
