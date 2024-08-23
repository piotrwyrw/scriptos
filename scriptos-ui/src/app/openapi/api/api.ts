export * from './groups.service';
import { GroupsService } from './groups.service';
export * from './user.service';
import { UserService } from './user.service';
export const APIS = [GroupsService, UserService];
