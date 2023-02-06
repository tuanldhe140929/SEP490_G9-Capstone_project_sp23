import { AuthResponse } from './../../../DTOS/AuthResponse';
import { StorageService } from 'src/app/services/storage.service';
import { AuthService } from './../../../services/auth.service';
import { FormBuilder, Validators } from '@angular/forms';
import { Component, OnInit } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { AppComponent } from 'src/app/app.component';
import { ManageAccountInfoService } from './../../../services/manage-account-info.service';

const ChangeNameUrl ="http://localhost:9000/private/profile/changeAccountName";
@Component({
  selector: 'app-change-name',
  templateUrl: './change-name.component.html',
  styleUrls: ['./change-name.component.css']
})

export class ChangeNameComponent implements OnInit {
  
  authResponse: AuthResponse = new AuthResponse();

  constructor(private FormBuilder: FormBuilder, private httpClient: HttpClient, private app: AppComponent, private storageService: StorageService, private manageAccountInfoService: ManageAccountInfoService){

  }
  ngOnInit(): void {
    this.authResponse = this.storageService.getAuthResponse();
  }

  public ChangeNameForm = this.FormBuilder.group({
    "newname":['',[Validators.required, Validators.minLength(3), Validators.maxLength(30)]]
  });
  
  get Form(){
    return this.ChangeNameForm.controls;
  }
  get newname(){
    return this.ChangeNameForm.controls.newname;
  }


   onChangeName(){
    this.manageAccountInfoService.onChangeName(this.ChangeNameForm.value).subscribe(
      data => {
        this.authResponse.username = data.username;
        this.storageService.saveUser(this.authResponse);
        console.log(data)
        
      },
      error =>{
        console.log(error)
      }
    )
   }
   
}
