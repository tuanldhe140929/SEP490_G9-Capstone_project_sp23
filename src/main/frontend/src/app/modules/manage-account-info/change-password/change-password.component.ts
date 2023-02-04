import { Component } from '@angular/core';
import { FormBuilder, Validators } from '@angular/forms';
import { HttpClient } from '@angular/common/http';
import { ManageAccountInfoService } from 'src/app/services/manage-account-info.service';

const changePasswordUrl = "http://localhost:9000/private/profile/changeAccountPassword";
@Component({
  selector: 'app-change-password',
  templateUrl: './change-password.component.html',
  styleUrls: ['./change-password.component.css']
})

export class ChangePasswordComponent {
	
	
	
	constructor(private formBuilder: FormBuilder, private httpClient: HttpClient, private manageAccountInfoService: ManageAccountInfoService){
		
	}
	
	get Form(){
		return this.changePasswordForm.controls;
	}
	
	get oldpassword(){
		return this.changePasswordForm.controls.oldpass;
	}
	
	get newpassword(){
		return this.changePasswordForm.controls.newpass;
	}
	
	get renewpassword(){
		return this.changePasswordForm.controls.renewpass;
	}
	
	public changePasswordForm = this.formBuilder.group({
		"oldpass": ['',[Validators.required, Validators.minLength(8), Validators.maxLength(30)]],
		"newpass": ['',[Validators.required, Validators.minLength(8), Validators.maxLength(30)]],
		"renewpass": ['',[Validators.required, Validators.minLength(8), Validators.maxLength(30)]]
	});
	
	public onPasswordMatch(){
		if(this.newpassword.value == this.renewpassword.value){
			return true;
		}else{
			return false;
		}
	}
	
	onChangePassword(){
		this.manageAccountInfoService.onChangePassword(this.changePasswordForm.value).subscribe(
			data => {
        console.log(data);
			},
			error =>{
        console.log(error);
			}
		)
	}
}
