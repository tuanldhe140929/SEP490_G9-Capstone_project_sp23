import { Component, Inject, OnInit } from '@angular/core';
import { FormBuilder, Validators } from '@angular/forms';
import { ToastrService } from 'ngx-toastr';
import { MAT_DIALOG_DATA } from '@angular/material/dialog';
import { AccountService } from 'src/app/services/account.service';
import { Account } from 'src/app/dtos/Account';

@Component({
  selector: 'app-add-staff',
  templateUrl: './add-staff.component.html',
  styleUrls: ['./add-staff.component.css']
})
export class AddStaffComponent implements OnInit{

  canCloseDialog: boolean;
  staff: Account;

  constructor(private formBuilder: FormBuilder, private accountService: AccountService, private toastr: ToastrService, @Inject(MAT_DIALOG_DATA) public data: {emailList: string[]}){}

  ngOnInit(): void {
    this.canCloseDialog = false;
  }

  addStaffForm = this.formBuilder.group({
    "email": ['',[Validators.required, Validators.email]],
    "password": ['',[Validators.required, Validators.minLength(8), Validators.maxLength(30)]],
    "repassword": ['',[Validators.required, Validators.minLength(8), Validators.maxLength(30)]]
  })

  get email(){
    return this.addStaffForm.controls.email;
  }

  get addform(){
    return this.addStaffForm.controls;
  }

  get password(){
    return this.addStaffForm.controls.password;
  }

  get repassword(){
    return this.addStaffForm.controls.repassword;
  }

  onPasswordMatch(): boolean{
    if(this.password.value==this.repassword.value){
      return true;
    }else{
      return false;
    }
  }

  checkEmailExists(){
    for(let i = 0; i < this.data.emailList.length; i++){
      if(this.email.value?.toLowerCase().trim()==this.data.emailList[i].trim().toLowerCase()){
        return true;
      }
    }
    return false;
  }

  onAddStaff(){
    if(this.addStaffForm.valid){
      if(!this.checkEmailExists()){
        this.canCloseDialog = true;
        this.accountService.addStaff(this.addStaffForm.value).subscribe(
          data => {
            this.toastr.success("Thêm nhân viên thành công!");
          }
        )
      }else{
        this.toastr.error("Email "+this.email.value+" đã tồn tại trong hệ thống!","Vui lòng dùng một email khác");
      }
      
    }
  }
}
