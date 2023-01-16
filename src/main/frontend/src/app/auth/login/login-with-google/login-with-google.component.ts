import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { AuthService } from 'src/app/services/auth.service';

@Component({
  selector: 'app-login-with-google',
  templateUrl: './login-with-google.component.html',
  styleUrls: ['./login-with-google.component.css']
})
export class LoginWithGoogleComponent implements OnInit {
	
	
	constructor(private route: ActivatedRoute, private authService: AuthService, private router: Router) {}

	ngOnInit(): void {
		var code = this.route.snapshot.queryParamMap.get('code');
		if (code != null) {
			this.authService.loginWithGoogle(code).subscribe(
				(data) => {
					console.log(data);
					this.router.navigate(['login']);
				}
				,error => {
				}
			);
		}
	}
}