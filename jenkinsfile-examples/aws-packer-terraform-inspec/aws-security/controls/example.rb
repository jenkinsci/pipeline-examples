
content = inspec.profile.file("output.json")
params = JSON.parse(content)

vpc_id = params['main_vpc_id']['value']
dmz_vpc_id = params['dmz_vpc_id']['value']

describe aws_vpc(vpc_id) do
  its('state') { should eq 'available' }
  # as we vary these based on the branch (master.tfvars & testing-defaults.tfvars)
  # we can't check the cidr without exporting the CIDR via output.json
  # its('cidr_block') { should eq '172.18.0.0/16' }
end

describe aws_vpc(dmz_vpc_id) do
  its('state') { should eq 'available' }
  # its('cidr_block') { should eq '172.19.0.0/16' }
end
