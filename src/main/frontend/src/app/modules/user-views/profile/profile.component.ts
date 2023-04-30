import { AppComponent } from 'src/app/app.component';
import { HttpClient } from '@angular/common/http';
import { AuthResponse } from 'src/app/dtos/AuthResponse';
import { FormBuilder, Validators } from '@angular/forms';
import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { Observable } from 'rxjs';
import { User } from 'src/app/dtos/User';
import { StorageService } from 'src/app/services/storage.service';
import { UserService } from 'src/app/services/user.service';
import { Role } from 'src/app/dtos/Role';



@Component({
  selector: 'app-profile',
  templateUrl: './profile.component.html',
  styleUrls: ['./profile.component.css']
})
export class ProfileComponent implements OnInit {

  authResponse: AuthResponse = new AuthResponse();
  user: User = new User();
  constructor(private FormBuilder: FormBuilder,
    private httpClient: HttpClient,
    private app: AppComponent,
    private storageService: StorageService,
    private userService: UserService,
    private router: Router) {

  }
  ngOnInit(): void {
    this.authResponse = this.storageService.getAuthResponse();
    this.getUserInfo();
    console.log(this.user.avatar);
  }

  public Profileform = this.FormBuilder.group({
    "newFirstName": ['', [Validators.required, Validators.minLength(3), Validators.maxLength(30)]],
    "newUsername": ['', [Validators.required, Validators.minLength(3), Validators.maxLength(30)]],
    "newLastName": ['', [Validators.required, Validators.minLength(3), Validators.maxLength(30)]]
  });

  get Form() {
    return this.Profileform.controls;
  }
  get newUsername() {
    return this.Profileform.controls.newUsername;
  }
  get newFirstName() {
    return this.Profileform.controls.newFirstName
  }
  get newLastName() {
    return this.Profileform.controls.newLastName;
  }
  public username = "";

  onChangeName() {
    this.Profileform.controls.newUsername.setValue(this.user.username)
    this.Profileform.controls.newFirstName.setValue(this.user.firstName)
    this.Profileform.controls.newLastName.setValue(this.user.lastName)
    this.userService.onChangeName(this.Profileform.value).subscribe(
      data => {
        console.log(data)
      },
      error => {
        console.log(error)
      }
    )
  }

  getUserInfo() {
    this.userService.getCurrentUserInfo().subscribe(
      data => {
        this.user = data;
        
        if (this.user.avatar == "" || this.user.avatar == null) {  
            this.ProfileImage.setAttribute('src',"http://ssl.gstatic.com/accounts/ui/avatar_2x.png") ;
        } else {
         this.ProfileImage.setAttribute('src',"http://localhost:9000/public/serveMedia/image?source=" + this.getEncodedUrl(this.user.avatar)) ;
        }
        
        console.log(data)
      },
      error => {
        console.log(error)
      }
    )
  }
  
  getEncodedUrl(source:string){
	  const encodedSource = encodeURIComponent(source.replace(/\\/g, '/').replace('//', '/').replace(/\(/g, '%28').replace(/\)/g, '%29'));
	  return encodedSource;
  }
  UpdateInfo() {
    if (this.UsernameInput != null && this.FirstNameInput != null && this.LastNameInput != null) {
      this.UsernameInput.removeAttribute('readonly');
      this.FirstNameInput.removeAttribute('readonly');
      this.LastNameInput.removeAttribute('readonly');
      this.Submitbtn.removeAttribute('disabled')
    }
    console.log("abc")
  }
  get Submitbtn(){
    return document.getElementById('Submitbtn') as HTMLButtonElement;
  }
  get ProfileImage(){
    return document.getElementById('Image') as HTMLImageElement;
  }
  get UsernameInput() {
    return document.getElementById('user_name') as HTMLInputElement;
  }
  get FirstNameInput() {
    return document.getElementById('first_name') as HTMLInputElement;
  }
  get LastNameInput() {
    return document.getElementById('last_name') as HTMLInputElement;
  }

  UploadProfileImage($event: any) {
    const file: File = $event.target.files[0];
    if (file) {
      const formData = new FormData();
      formData.append("thumbnail", file);
      const upload$ = this.userService.uploadProfileImage(file).subscribe(
        data => {
          this.user.avatar = data;
          console.log(data);
          this.ProfileImage.setAttribute('src',"" );
          this.ProfileImage.setAttribute('src',"http://localhost:9000/public/serveMedia/serveProfileImage?userId=" + this.user.id);
        },
        error => {
          console.log(error)
        }
      )
    }
  }
  isSeller(): boolean {
    const sellerRole: Role = {id: 4, name: 'ROLE_SELLER'}; 
    return this.user.roles.some(role => role.id === sellerRole.id);
  }
  formatTime(createdDate: Date){
    const timestamp = createdDate;
    const date = new Date(timestamp);
    
    const formattedDate = date.getDate().toString().padStart(2, '0') + '-' +
                          (date.getMonth() + 1).toString().padStart(2, '0') + '-' +
                          date.getFullYear().toString();
    
    const formattedTime = date.getHours().toString().padStart(2, '0') + ':' +
                          date.getMinutes().toString().padStart(2, '0') + ':' +
                          date.getSeconds().toString().padStart(2, '0');
    
    const formattedTimestamp = formattedDate + ' ' + formattedTime;
    console.log(formattedTimestamp);
    return formattedTimestamp;
  }

  changePassword() {
    this.router.navigate(['changepassword']);
  }

  becomeSeller() {
    this.router.navigate(['becomeSeller']);
  }

  viewPayouts() {
    this.router.navigate(['payout']);
  }
  viewPurchased() {
    this.router.navigate(['purchased']);
  }

}
