import { Component, Inject, OnInit } from '@angular/core';
import { FormBuilder, Validators } from '@angular/forms';
import { MAT_DIALOG_DATA } from '@angular/material/dialog';
import { Router } from '@angular/router';
import { ToastrService } from 'ngx-toastr';
import { User } from 'src/app/DTOS/User';
import { ViolationType } from 'src/app/DTOS/ViolationType';
import { ReportService } from 'src/app/services/report.service';
import { StorageService } from 'src/app/services/storage.service';
import { UserService } from 'src/app/services/user.service';
import { ViolationTypeService } from 'src/app/services/violation-type.service';


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
    private vioTypeService: ViolationTypeService,
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
    this.getAllVioTypes();
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
    this.vioTypeService.getAllTypes().subscribe(
      response => {
        this.vioTypeList = response;
      }
    )
  }

  toLogin(){
    this.router.navigate(['login']);
  }

  toSendReport(){
      this.reportService.sendReport(this.data.productId, this.userId, this.description, this.violationTypeId).subscribe(
        data =>{
          console.log(data);
        }
      )
      this.toastr.success("Gửi báo cáo thành công");
  }

  onDescriptionChange(){
    console.log(this.description);
  }

  onChange(value: string){
    this.violationTypeId = Number(value);
    console.log("proid:"+this.data.productId+" userid:"+this.data.userId+" vio:"+this.violationTypeId+" desc:"+this.description);
  }
}
