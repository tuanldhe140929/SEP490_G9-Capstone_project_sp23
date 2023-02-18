import { Component, OnInit } from '@angular/core';
import { FormBuilder, Validators } from '@angular/forms';
import { ToastrService } from 'ngx-toastr';
import { ForAdminService } from 'src/app/services/for-admin.service';


@Component({
  selector: 'app-add-staff',
  templateUrl: './add-staff.component.html',
  styleUrls: ['./add-staff.component.css']
})
export class AddStaffComponent implements OnInit{

  constructor(private formBuilder: FormBuilder, private forAdminService: ForAdminService, private toastr: ToastrService){}

  ngOnInit(): void {
    
  }

  addStaffForm = this.formBuilder.group({
    "email": ['',[Validators.required, Validators.email]],
    "password": ['',[Validators.required, Validators.minLength(8), Validators.maxLength(30)]],
    "repassword": ['',[Validators.required, Validators.minLength(8), Validators.maxLength(30)]]
  })

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

  onAddStaff(){
    if(this.addStaffForm.valid){
      this.forAdminService.addStaff(this.addStaffForm.value).subscribe(
        data => {
          console.log(data);
          this.toastr.success("Thêm nhân viên thành công!");
        }
      )
    }
  }
}
