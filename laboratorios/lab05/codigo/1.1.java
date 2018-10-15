    public static int punto3(int i ,int j ,String s1 ,String s2){
        if(i==0 || j==0){
            return 0;
        }
        boolean anterior = i < s1.length() && j < s2.length();
        if(anterior && s1.charAt(i)== s2.charAt(j)){
            return 1+punto3(i-1,j-1,s1,s2);
        }
        int ni = punto3(i-1,j,s1,s2);
        int nj = punto3(i,j-1,s1,s2);
        return Math.max(ni,nj);
    }
