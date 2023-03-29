import { Account } from "./Account";
import { Product } from "./Product";
import { User } from "./User";
import { ViolationType } from "./ViolationType";

export class Report {
      user: User;
      product: Product;
      version: String;
      description: string;
      created_date: string;
      violation_types: ViolationType;
      status: string
}
