package server;

import java.io.InputStream;

import common.AddNoteResult;
import common.ChangePasswdResult;
import common.CloudFile;
import common.Credential;
import common.DeleteFileResult;
import common.DeleteNoteResult;
import common.DownloadFileResult;
import common.FileDirectoryResult;
import common.LoginResult;
import common.Note;
import common.NoteListResult;
import common.RegisterResult;
import common.RenameFileResult;
import common.UploadFileResult;

/**
 * 服务器端BLL提供接口
 * @author yzj
 * 登录
 * 注册
 * 修改密码
 */
public interface IBusinessLogic {
	/**
	 * 登录服务接口
	 * @param cred 身份验证
	 * @return 登录结果
	 */
	public LoginResult login(Credential cred);
	/**
	 * 注册服务接口
	 * @param cred 身份验证
	 * @return 注册结果
	 */
	public RegisterResult register(Credential cred);
	/**
	 * 修改密码服务接口
	 * @param cred 
	 * @param newPassword
	 * @return 修改结果
	 */
	public ChangePasswdResult changePasswd(Credential cred, String newPassword);
	/**
	 * 上传文件服务接口
	 * @param cloudFile
	 * @param content
	 * @return 上传结果
	 */
	public UploadFileResult uploadFile(CloudFile cloudFile, InputStream content);
	/**
	 * 获取指定用户文件目录
	 * @param targetID 目标用户
	 * @param userID   当前用户
	 * @return 获取结果和目录HashSet
	 */
	public FileDirectoryResult getDirectory(String targetID,String userID);
	/**
	 * 删除指定文件
	 * @param fileID
	 * @param userID
	 * @return 删除结果（成功删除为OK，文件不存在或无权限为wrong）
	 */
	public DeleteFileResult deleteFile(String fileID,String userID);
	/**
	 * 下载指定文件
	 * @param fileID
	 * @param userID
	 * @return 下载内容
	 */
	public DownloadFileResult downloadFile(String fileID,String userID);
	/**
	 * 重命名指定文件
	 * @param fileID
	 * @param newFilename
	 * @param userID
	 * @return 重命名结果
	 */
	public RenameFileResult renameFile(String fileID,String newFilename,String userID);
	/**
	 * 增加备注
	 * @param note
	 * @return 结果
	 */
	public AddNoteResult addNote(Note note);
	/**
	 * 删除备注
	 * @param note, including noteID and fileID
	 * @param userID
	 * @return 结果
	 */
	public DeleteNoteResult deleteNote(Note note,String userID);
	/**
	 * 获取指定文件的备注列表
	 * @param fileID
	 * @param userID
	 * @return 结果
	 */
	public NoteListResult getNoteList(String fileID,String userID);
}
