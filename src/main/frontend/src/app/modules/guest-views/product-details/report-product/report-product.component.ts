import { Component, Inject, OnInit } from '@angular/core';
import { FormBuilder, Validators } from '@angular/forms';
import { MAT_DIALOG_DATA } from '@angular/material/dialog';
import { Router } from '@angular/router';
import { ToastrService } from 'ngx-toastr';
import { User } from 'src/app/dtos/User';
import { ViolationType } from 'src/app/dtos/ViolationType';
import { ReportService } from 'src/app/services/report.service';
import { StorageService } from 'src/app/services/storage.service';
import { UserService } from 'src/app/services/user.service';
import { ViolationService } from 'src/app/services/violation.service';


@Component({
  selector: 'app-report-product',
  templateUrl: './report-product.component.html',
  styleUrls: ['./report-product.component.css']
})
export class ReportProductComponent implements OnInit{
  dialog: any;
  showMessage = false;
  loginStatus = false;
  user: User = new User;

  constructor(
    private violationService: ViolationService,
    private reportService: ReportService,
    private formBuilder: FormBuilder,
    private storageService: StorageService,
    private router: Router,
    private toastr: ToastrService,
    private userService:UserService,
    @Inject(MAT_DIALOG_DATA) public data: any){}

    violationTypeId: number;
    violationName: string;
    productId: number;
    userId: number;
    description: string;

  vioTypeList: ViolationType[] = [];

  ngOnInit(): void {
    this.violationService.getAllTypes().subscribe(
      response => {
        this.vioTypeList = response;
      }
    )
    this.productId = this.data.productId;
    this.userService.getCurrentUserInfo().subscribe(
      data => {
        this.user = data;
        this.userId = data.id;
      }
    )
    this.description = "";
    this.violationTypeId = 0;

  }

  addReportForm = this.formBuilder.group({
    violationtype: ['',[Validators.required]],
    description: ['',[Validators.required]]
  })

  get form(){
    return this.addReportForm.controls;
  }

  get vioTypeId(){
    return this.addReportForm.controls.violationtype;
  }

  get desc(){
    return this.addReportForm.controls.description;
  }

  getAllVioTypes(){
    this.violationService.getAllTypes().subscribe(
      response => {
        this.vioTypeList = response;
      }
    )
  }

  toLogin(){
    this.router.navigate(['login']);
  }

  toSendReport(){
      this.reportService.sendReport(this.data.productId, this.userId, this.data.version , this.description, this.violationTypeId).subscribe(
        data =>{
          console.log(data);
        }
      )
      this.toastr.success("Gửi báo cáo thành công");
  }

  descriptionEmpty(): boolean{
    if(this.description.length<=0){
      return true
    }else{
      return false
    }
  }

  descriptionExceed(): boolean{
    if(this.description.length<10||this.description.length>255){
      return true
    }else{
      return false
    }
  }

  onDescriptionChange(){
    console.log(this.description);
  }

  onChange(value: string){
    this.violationTypeId = Number(value);
    console.log("proid:"+this.data.productId+" userid:"+this.data.userId+" vio:"+this.violationTypeId+" desc:"+this.description);
  }
  
}
