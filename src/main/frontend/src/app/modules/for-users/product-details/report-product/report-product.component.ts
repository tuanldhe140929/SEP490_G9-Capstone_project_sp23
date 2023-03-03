import { Component, OnInit } from '@angular/core';
import { FormBuilder, Validators } from '@angular/forms';
import { ViolationType } from 'src/app/DTOS/ViolationType';
import { ReportService } from 'src/app/services/report.service';
import { ViolationTypeService } from 'src/app/services/violation-type.service';

@Component({
  selector: 'app-report-product',
  templateUrl: './report-product.component.html',
  styleUrls: ['./report-product.component.css']
})
export class ReportProductComponent implements OnInit{

  constructor(
    private vioTypeService: ViolationTypeService,
    private reportService: ReportService,
    private formBuilder: FormBuilder){}

  vioTypeList: ViolationType[] = [];

  ngOnInit(): void {
    this.getAllVioTypes();
  }

  addReportForm = this.formBuilder.group({
    violation_type_id: ['',[Validators.required]],
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
    if(this.addReportForm.valid){
      this.reportService.addReport(this.addReportForm.value).subscribe(
        data => {
          console.log(data);
        }
      )
    }
  }
}
