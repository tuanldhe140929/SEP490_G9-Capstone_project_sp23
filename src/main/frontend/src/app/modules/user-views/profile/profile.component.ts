import { AppComponent } from 'src/app/app.component';
import { HttpClient } from '@angular/common/http';
import { AuthResponse } from 'src/app/dtos/AuthResponse';
import { FormBuilder, Validators } from '@angular/forms';
import { ChangeDetectorRef, Component, OnInit, ViewChild } from '@angular/core';
import { Router } from '@angular/router';
import { Observable } from 'rxjs';
import { User } from 'src/app/dtos/User';
import { StorageService } from 'src/app/services/storage.service';
import { UserService } from 'src/app/services/user.service';
import { Role } from 'src/app/dtos/Role';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

const MSG105 = 'Định dạng này không được hỗ trợ';
const MSG1001 = 'Tên người dùng có độ dài từ 3 đến 30 kí tự';
const MSG1002 = 'Tên có độ dài từ 1 đến 50 kí tự';
const MSG1003 = 'Họ có độ dài từ 1 đến 50 kí tự';
const MSG100 = 'Tên người dùng không được để trống';
const MSG101 = 'Tên không được để trống';
const MSG102 = 'Họ không được để trống';
const MSG103 = 'Tên người dùng không được phép chứa ký tự đặc biệt';
const MSG104 = 'Tên không được phép chứa ký tự đặc biệt'
const MSG106 = 'Họ không được phép chứa ký tự đặc biệt'
const IMAGE_EXTENSIONS = ['image/png', 'image/jpeg', 'image/svg+xml'];
const regex = /[~`!@#$%^&*()-_=+\[\]\\|{};:'",<.>\/?]/;
@Component({
  selector: 'app-profile',
  templateUrl: './profile.component.html',
  styleUrls: ['./profile.component.css']
})
export class ProfileComponent implements OnInit {
  @ViewChild('changeName', { static: false }) private changeName: any;
  @ViewChild('notAcceptFormatModal', { static: false }) private notAcceptFormatModal: any;
  @ViewChild('fileSizeErrorModal', { static: false }) private fileSizeErrorModal: any;

  authResponse: AuthResponse = new AuthResponse();
  user: User = new User();
  fileError = "";
  nameError = '';
  SPECIAL_CHARACTERS = "~`!@#$%^&*()-_=+\\|[{]};:'\",<.>/?";
  constructor(private FormBuilder: FormBuilder,
    private httpClient: HttpClient,
    private app: AppComponent,
    private storageService: StorageService,
    private modalService: NgbModal,
    private cd: ChangeDetectorRef,
    private userService: UserService,
    private router: Router) {
  }
  ngOnInit(): void {
    this.authResponse = this.storageService.getAuthResponse();
    this.getUserInfo();
    console.log(this.user.avatar);
  }

  public Profileform = this.FormBuilder.group({
    "newFirstName": ['', [Validators.required, Validators.minLength(1), Validators.maxLength(50)]],
    "newUsername": ['', [Validators.required, Validators.minLength(3), Validators.maxLength(30)]],
    "newLastName": ['', [Validators.required, Validators.minLength(1), Validators.maxLength(50)]]
  });

  get Form() {
    return this.Profileform.controls;
  }
  get newUsername() {
    return document.getElementById("user_name") as HTMLInputElement
  }
  get newFirstName() {
    return document.getElementById("first_name") as HTMLInputElement

  }
  get newLastName() {
    return document.getElementById("last_name") as HTMLInputElement
  }
  public username = "";

  removeSpaces(text: string) {
    return text.replace(/\s+/g, ' ').trim();
  }

  
  onChangeName() {
    if (this.newUsername.value == null || this.newUsername.value == '') {
      this.errors.push(MSG100);
    }
    if (this.newUsername.value != null)
      if (this.newUsername.value.length < 3 || this.newUsername.value.length > 30) {
        this.errors.push(MSG1001);

      }
    if (this.newFirstName.value != null)
      if (this.newFirstName.value.length > 50) {
        this.errors.push(MSG1002);
      }
    if (this.newLastName.value != null)
      if (this.newLastName.value.length > 50) {
        this.errors.push(MSG1003);
      }
      const SPECIAL_CHARACTERS_REGEX = /[~`!@#$%^&*()_\-+=[\]{}|\\;:'",<.>/?]/;
    if (this.newUsername != null)
    if (this.newUsername && SPECIAL_CHARACTERS_REGEX.test(this.newUsername.value)) {
      this.errors.push(MSG103);
    }
    
    if (this.newFirstName != null)
      if (this.newFirstName && SPECIAL_CHARACTERS_REGEX.test(this.newFirstName.value)) {
        this.errors.push(MSG104);
      }
    if (this.newLastName != null)
      if (this.newLastName && SPECIAL_CHARACTERS_REGEX.test(this.newLastName.value)) {
        this.errors.push(MSG106);
      }
    if (this.errors.length > 0) {
      this.openNameErrorModal();
      return;
    }
    console.log(this.newLastName.value);

    this.Profileform.controls.newUsername.setValue(this.user.username)
    this.Profileform.controls.newFirstName.setValue(this.user.firstName)
    this.Profileform.controls.newLastName.setValue(this.user.lastName)
    this.userService.onChangeName(this.Profileform.value).subscribe(
      data => {
        console.log(data)
      },
      error => {
        this.nameError = 'Cập nhật thông tin không thành công';
        this.openNameErrorModal();
        console.log(error);
      }
    )
  }

  getUserInfo() {
    this.userService.getCurrentUserInfo().subscribe(
      (data) => {
        this.user = data;

        if (this.user.avatar == "" || this.user.avatar == null) {
          this.ProfileImage.setAttribute('src', "http://ssl.gstatic.com/accounts/ui/avatar_2x.png");
        } else {
          this.ProfileImage.setAttribute('src', "http://localhost:9000/public/serveMedia/image?source=" + this.getEncodedUrl(this.user.avatar));
        }

        console.log(data)
      },
      (error) => {
        console.log(error)
      }
    )
  }

  getEncodedUrl(source: string) {
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
  }
  get Submitbtn() {
    return document.getElementById('Submitbtn') as HTMLButtonElement;
  }
  get ProfileImage() {
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

  fileFormatError = MSG105;
  UploadProfileImage($event: any) {
    const file: File = $event.target.files[0];
    if (file.name.length > 100 || file.name.length < 1) {
      this.fileError = "Tên tệp từ 1 đến 100 kí tự";
      this.openFileSizeErrorModal();
      return;
    }
    if (file) {
      console.log(file.type);
      if (!this.checkFileType(file.type, IMAGE_EXTENSIONS)) {
        this.openFormatErrorModal();
      } else {
        const formData = new FormData();
        formData.append("thumbnail", file);
        const upload$ = this.userService.uploadProfileImage(file).subscribe(
          (data: string) => {
            this.user.avatar = data;
            console.log(data);
            this.ProfileImage.setAttribute('src', "");
            this.ProfileImage.setAttribute('src', "http://localhost:9000/public/serveMedia/serveProfileImage?userId=" + this.user.id);
          },
          (error: any) => {
            this.fileError = 'Tải lên hình ảnh không thành công';
            this.openFileSizeErrorModal();
          }
        )
      }
    }
  }
  isSeller(): boolean {
    const sellerRole: Role = { id: 4, name: 'ROLE_SELLER' };
    return this.user.roles.some(role => role.id === sellerRole.id);
  }
  formatTime(createdDate: Date) {
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
  errors: string[] = [];
  dismissError() {
    this.errors = [];
    this.cd.detectChanges();
    this.modalService.dismissAll();
  }
  //Lien quan den upload avatar
  openFileSizeErrorModal() {
    this.modalService.open(this.fileSizeErrorModal, { centered: true });
  }
  checkFileType(fileType: string, acceptType: string[]): boolean {
    return acceptType.includes(fileType);
  }
  openFormatErrorModal() {
    this.modalService.open(this.notAcceptFormatModal, { centered: true });
  }
  openNameErrorModal() {
    this.modalService.open(this.changeName, { centered: true });
  }

}
