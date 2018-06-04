
package oauth;

import javax.annotation.Generated;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

@Generated("org.jsonschema2pojo")
public class TokenResponse {

    @SerializedName("issued_at")
    @Expose
    private String issuedAt;
    @Expose
    private String scope;
    @SerializedName("application_name")
    @Expose
    private String applicationName;
    @SerializedName("refresh_token_issued_at")
    @Expose
    private String refreshTokenIssuedAt;
    @Expose
    private String status;
    @SerializedName("user_company_id")
    @Expose
    private String userCompanyId;
    @SerializedName("refresh_token_status")
    @Expose
    private String refreshTokenStatus;
    @SerializedName("api_product_list")
    @Expose
    private String apiProductList;
    @SerializedName("expires_in")
    @Expose
    private String expiresIn;
    @SerializedName("user_email")
    @Expose
    private String userEmail;
    @SerializedName("organization_id")
    @Expose
    private String organizationId;
    @SerializedName("token_type")
    @Expose
    private String tokenType;
    @SerializedName("refresh_token")
    @Expose
    private String refreshToken;
    @SerializedName("client_id")
    @Expose
    private String clientId;
    @SerializedName("access_token")
    @Expose
    private String accessToken;
    @SerializedName("organization_name")
    @Expose
    private String organizationName;
    @SerializedName("refresh_token_expires_in")
    @Expose
    private String refreshTokenExpiresIn;
    @SerializedName("refresh_count")
    @Expose
    private String refreshCount;

    /**
     * 
     * @return
     *     The issuedAt
     */
    public String getIssuedAt() {
        return issuedAt;
    }

    /**
     * 
     * @param issuedAt
     *     The issued_at
     */
    public void setIssuedAt(String issuedAt) {
        this.issuedAt = issuedAt;
    }

    public TokenResponse withIssuedAt(String issuedAt) {
        this.issuedAt = issuedAt;
        return this;
    }

    /**
     * 
     * @return
     *     The scope
     */
    public String getScope() {
        return scope;
    }

    /**
     * 
     * @param scope
     *     The scope
     */
    public void setScope(String scope) {
        this.scope = scope;
    }

    public TokenResponse withScope(String scope) {
        this.scope = scope;
        return this;
    }

    /**
     * 
     * @return
     *     The applicationName
     */
    public String getApplicationName() {
        return applicationName;
    }

    /**
     * 
     * @param applicationName
     *     The application_name
     */
    public void setApplicationName(String applicationName) {
        this.applicationName = applicationName;
    }

    public TokenResponse withApplicationName(String applicationName) {
        this.applicationName = applicationName;
        return this;
    }

    /**
     * 
     * @return
     *     The refreshTokenIssuedAt
     */
    public String getRefreshTokenIssuedAt() {
        return refreshTokenIssuedAt;
    }

    /**
     * 
     * @param refreshTokenIssuedAt
     *     The refresh_token_issued_at
     */
    public void setRefreshTokenIssuedAt(String refreshTokenIssuedAt) {
        this.refreshTokenIssuedAt = refreshTokenIssuedAt;
    }

    public TokenResponse withRefreshTokenIssuedAt(String refreshTokenIssuedAt) {
        this.refreshTokenIssuedAt = refreshTokenIssuedAt;
        return this;
    }

    /**
     * 
     * @return
     *     The status
     */
    public String getStatus() {
        return status;
    }

    /**
     * 
     * @param status
     *     The status
     */
    public void setStatus(String status) {
        this.status = status;
    }

    public TokenResponse withStatus(String status) {
        this.status = status;
        return this;
    }

    /**
     * 
     * @return
     *     The userCompanyId
     */
    public String getUserCompanyId() {
        return userCompanyId;
    }

    /**
     * 
     * @param userCompanyId
     *     The user_company_id
     */
    public void setUserCompanyId(String userCompanyId) {
        this.userCompanyId = userCompanyId;
    }

    public TokenResponse withUserCompanyId(String userCompanyId) {
        this.userCompanyId = userCompanyId;
        return this;
    }

    /**
     * 
     * @return
     *     The refreshTokenStatus
     */
    public String getRefreshTokenStatus() {
        return refreshTokenStatus;
    }

    /**
     * 
     * @param refreshTokenStatus
     *     The refresh_token_status
     */
    public void setRefreshTokenStatus(String refreshTokenStatus) {
        this.refreshTokenStatus = refreshTokenStatus;
    }

    public TokenResponse withRefreshTokenStatus(String refreshTokenStatus) {
        this.refreshTokenStatus = refreshTokenStatus;
        return this;
    }

    /**
     * 
     * @return
     *     The apiProductList
     */
    public String getApiProductList() {
        return apiProductList;
    }

    /**
     * 
     * @param apiProductList
     *     The api_product_list
     */
    public void setApiProductList(String apiProductList) {
        this.apiProductList = apiProductList;
    }

    public TokenResponse withApiProductList(String apiProductList) {
        this.apiProductList = apiProductList;
        return this;
    }

    /**
     * 
     * @return
     *     The expiresIn
     */
    public String getExpiresIn() {
        return expiresIn;
    }

    /**
     * 
     * @param expiresIn
     *     The expires_in
     */
    public void setExpiresIn(String expiresIn) {
        this.expiresIn = expiresIn;
    }

    public TokenResponse withExpiresIn(String expiresIn) {
        this.expiresIn = expiresIn;
        return this;
    }

    /**
     * 
     * @return
     *     The userEmail
     */
    public String getUserEmail() {
        return userEmail;
    }

    /**
     * 
     * @param userEmail
     *     The user_email
     */
    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public TokenResponse withUserEmail(String userEmail) {
        this.userEmail = userEmail;
        return this;
    }

    /**
     * 
     * @return
     *     The organizationId
     */
    public String getOrganizationId() {
        return organizationId;
    }

    /**
     * 
     * @param organizationId
     *     The organization_id
     */
    public void setOrganizationId(String organizationId) {
        this.organizationId = organizationId;
    }

    public TokenResponse withOrganizationId(String organizationId) {
        this.organizationId = organizationId;
        return this;
    }

    /**
     * 
     * @return
     *     The tokenType
     */
    public String getTokenType() {
        return tokenType;
    }

    /**
     * 
     * @param tokenType
     *     The token_type
     */
    public void setTokenType(String tokenType) {
        this.tokenType = tokenType;
    }

    public TokenResponse withTokenType(String tokenType) {
        this.tokenType = tokenType;
        return this;
    }

    /**
     * 
     * @return
     *     The refreshToken
     */
    public String getRefreshToken() {
        return refreshToken;
    }

    /**
     * 
     * @param refreshToken
     *     The refresh_token
     */
    public void setRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }

    public TokenResponse withRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
        return this;
    }

    /**
     * 
     * @return
     *     The clientId
     */
    public String getClientId() {
        return clientId;
    }

    /**
     * 
     * @param clientId
     *     The client_id
     */
    public void setClientId(String clientId) {
        this.clientId = clientId;
    }

    public TokenResponse withClientId(String clientId) {
        this.clientId = clientId;
        return this;
    }

    /**
     * 
     * @return
     *     The accessToken
     */
    public String getAccessToken() {
        return accessToken;
    }

    /**
     * 
     * @param accessToken
     *     The access_token
     */
    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public TokenResponse withAccessToken(String accessToken) {
        this.accessToken = accessToken;
        return this;
    }

    /**
     * 
     * @return
     *     The organizationName
     */
    public String getOrganizationName() {
        return organizationName;
    }

    /**
     * 
     * @param organizationName
     *     The organization_name
     */
    public void setOrganizationName(String organizationName) {
        this.organizationName = organizationName;
    }

    public TokenResponse withOrganizationName(String organizationName) {
        this.organizationName = organizationName;
        return this;
    }

    /**
     * 
     * @return
     *     The refreshTokenExpiresIn
     */
    public String getRefreshTokenExpiresIn() {
        return refreshTokenExpiresIn;
    }

    /**
     * 
     * @param refreshTokenExpiresIn
     *     The refresh_token_expires_in
     */
    public void setRefreshTokenExpiresIn(String refreshTokenExpiresIn) {
        this.refreshTokenExpiresIn = refreshTokenExpiresIn;
    }

    public TokenResponse withRefreshTokenExpiresIn(String refreshTokenExpiresIn) {
        this.refreshTokenExpiresIn = refreshTokenExpiresIn;
        return this;
    }

    /**
     * 
     * @return
     *     The refreshCount
     */
    public String getRefreshCount() {
        return refreshCount;
    }

    /**
     * 
     * @param refreshCount
     *     The refresh_count
     */
    public void setRefreshCount(String refreshCount) {
        this.refreshCount = refreshCount;
    }

    public TokenResponse withRefreshCount(String refreshCount) {
        this.refreshCount = refreshCount;
        return this;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(issuedAt).append(scope).append(applicationName).append(refreshTokenIssuedAt).append(status).append(userCompanyId).append(refreshTokenStatus).append(apiProductList).append(expiresIn).append(userEmail).append(organizationId).append(tokenType).append(refreshToken).append(clientId).append(accessToken).append(organizationName).append(refreshTokenExpiresIn).append(refreshCount).toHashCode();
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if ((other instanceof TokenResponse) == false) {
            return false;
        }
        TokenResponse rhs = ((TokenResponse) other);
        return new EqualsBuilder().append(issuedAt, rhs.issuedAt).append(scope, rhs.scope).append(applicationName, rhs.applicationName).append(refreshTokenIssuedAt, rhs.refreshTokenIssuedAt).append(status, rhs.status).append(userCompanyId, rhs.userCompanyId).append(refreshTokenStatus, rhs.refreshTokenStatus).append(apiProductList, rhs.apiProductList).append(expiresIn, rhs.expiresIn).append(userEmail, rhs.userEmail).append(organizationId, rhs.organizationId).append(tokenType, rhs.tokenType).append(refreshToken, rhs.refreshToken).append(clientId, rhs.clientId).append(accessToken, rhs.accessToken).append(organizationName, rhs.organizationName).append(refreshTokenExpiresIn, rhs.refreshTokenExpiresIn).append(refreshCount, rhs.refreshCount).isEquals();
    }

}
