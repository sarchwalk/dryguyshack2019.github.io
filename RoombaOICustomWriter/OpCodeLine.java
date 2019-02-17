public class OpCodeLine
{
	String opCode;
	String opCodeComment;
	
	public String getOpCode()
	{
		return opCode;
	}
	
	public String getOpCodeComment()
	{
		return opCodeComment;
	}
	
	public String toFileString()
	{
		return opCode + ";" + opCodeComment;
	}
	
	
	public OpCodeLine(String opCode, String opCodeComment)
	{
		this.opCode = opCode;
		this.opCodeComment = opCodeComment;
	}
}