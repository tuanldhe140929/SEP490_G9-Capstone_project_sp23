import { Component, Inject , OnInit} from '@angular/core';
import { MatDialog, MAT_DIALOG_DATA } from '@angular/material/dialog';
import { ToastrService } from 'ngx-toastr';
import { Report } from 'src/app/dtos/Report';
import { User } from 'src/app/dtos/User';
import { ViolationType } from 'src/app/dtos/ViolationType';
import { ProductService } from 'src/app/services/product.service';
import { ReportService } from 'src/app/services/report.service';
import { UserService } from 'src/app/services/user.service';
import { ViolationService} from 'src/app/services/violation.service';
import {AfterViewInit, ViewChild} from '@angular/core';
import {MatPaginator} from '@angular/material/paginator';
import {MatTableDataSource} from '@angular/material/table';
import {SelectionModel} from '@angular/cdk/collections';
import { ReportDescriptionComponent } from '../report-description/report-description.component';
import { Product } from 'src/app/dtos/Product';

interface ReportEntity{
  userId: number,
  username: string,
  version: string,
  violationTypeName: string,
  description: string,
  createdDate: string,
  status: String,
  checked: boolean,
}

@Component({
  selector: 'app-update-report-status',
  templateUrl: './update-report-status.component.html',
  styleUrls: ['./update-report-status.component.css']
})
export class UpdateReportStatusComponent implements AfterViewInit{
  displayedColumns: string[] = ['username',  'violationType', 'description','checkbox'];
  reportList: Report[] = [];
  userList: User[] = [];
  violationTypeList: ViolationType[] = [];
  reportEntityList: ReportEntity[] = [];
  reported: boolean = false;
  currentVer: string;
  latestVer: Product;
  dataSource: MatTableDataSource<ReportEntity>;
  @ViewChild(MatPaginator) paginator: MatPaginator;
  selection = new SelectionModel<ReportEntity>(true, []);

  constructor(
    @Inject(MAT_DIALOG_DATA) public dataInjected: {productId: number, version: string, status: string}, 
    private reportService: ReportService,
    private userService: UserService,
    private violationService: ViolationService,
    private productService: ProductService,
    private dialog: MatDialog,
    private toastr: ToastrService 
  ){
    this.reportService.getByProductVersionAndStatus(dataInjected.productId, dataInjected.version, dataInjected.status).subscribe(reports => {
      this.reportList = reports
      this.userService.getAllUsers().subscribe(users => {
        this.userList = users
        this.violationService.getAllTypes().subscribe(violationtypes => {
          this.currentVer = dataInjected.version;
          this.violationTypeList = violationtypes
          const reportEntities : ReportEntity[] = reports.map((report: Report) => {
            const user = users.find((u: User) => {u.id === report.user.id});
            const violationType = violationtypes.find((v: ViolationType) => {v.id === report.violation_types.id});
            return{
              userId: report.user.id,
              username: report.user.username,
              version: report.version,
              violationTypeName: report.violation_types.name,
              description: report.description,
              createdDate: report.created_date,
              status: report.status,
              checked: false,
            }
          })
          this.reportEntityList = reportEntities;
        this.dataSource = new MatTableDataSource(reportEntities);
        this.dataSource.paginator = this.paginator;
        })
        
        
      })
      
    })
  }

  openReportDescription(username: string, version: string, violationTypeName: string, description: string, reportedDate: string){
    const dialogRef = this.dialog.open(ReportDescriptionComponent, {
      data: {
        username: username,
        version: version,
        violationTypeName: violationTypeName,
        description: description,
        reportedDate: reportedDate
      },
      width: '50%'
    });
    dialogRef.afterClosed().subscribe(result => {
      console.log(`Dialog result: ${result}`);
    });
  }

  updateStatus(){
    let data = this.dataSource.data;
    let userIdList = [];
    let statusList = [];
    for(let i = 0; i<data.length;i++){
      let userData = data[i];
      userIdList.push(userData.userId);
      statusList.push(userData.checked ? "ACCEPTED":"DENIED");
    }
    this.reportService.updateReportStatus(this.dataInjected.productId, this.dataInjected.version, userIdList, statusList).subscribe(data => {
      console.log(data);
      this.toastr.success('Cập nhật trạng thái báo cáo thành công');
    })
    
  }

  ngAfterViewInit() {
    
  }

}



